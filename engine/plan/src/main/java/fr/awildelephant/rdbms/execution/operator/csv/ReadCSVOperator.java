package fr.awildelephant.rdbms.execution.operator.csv;

import fr.awildelephant.rdbms.execution.executor.TemporaryStorage;
import fr.awildelephant.rdbms.execution.operator.Operator;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.storage.data.column.AppendableColumn;
import fr.awildelephant.rdbms.storage.data.table.Table;
import fr.awildelephant.rdbms.storage.data.table.WriteableTable;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.GZIPInputStream;

import static fr.awildelephant.rdbms.storage.data.table.TableFactory.simpleTable;

public final class ReadCSVOperator implements Operator {

    private final String filePath;
    private final Schema outputSchema;

    public ReadCSVOperator(final String filePath, final Schema outputSchema) {
        this.filePath = filePath;
        this.outputSchema = outputSchema;
    }

    @Override
    public Table compute(TemporaryStorage unused) {
        final File file = new File(filePath);

        return readTable(file);
    }

    private Table readTable(File file) {
        // TODO: check if the file is gzip-compressed or not
        try (final BufferedReader reader = readGZIPFile(file);
             final CSVParser csvParser = CSVFormat.DEFAULT.withDelimiter('|').parse(reader)) {
            final int numberOfTuples = (int) csvParser.getRecordNumber();
            final WriteableTable outputTable = simpleTable(outputSchema, numberOfTuples);

            final List<CellParser> parsers = outputSchema.columnMetadataList().stream()
                    .map(ColumnMetadata::domain)
                    .map(CellParserFactory::build)
                    .toList();

            final List<AppendableColumn> columns = outputTable.columns();
            final int numberOfColumns = columns.size();
            for (CSVRecord record : csvParser) {
                for (int i = 0; i < numberOfColumns; i++) {
                    final AppendableColumn column = columns.get(i);
                    final CellParser cellParser = parsers.get(i);
                    final String cell = record.get(i);

                    column.add(cellParser.apply(cell));
                }
            }

            return outputTable;
        } catch (IOException e) {
            throw new CSVFileReadException(filePath, e);
        }
    }

    private static BufferedReader readGZIPFile(File file) throws IOException {
        return new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(file))));
    }
}
