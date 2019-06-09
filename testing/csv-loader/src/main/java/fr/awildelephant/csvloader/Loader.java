package fr.awildelephant.csvloader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

public class Loader {

    private final Connection connection;

    public Loader(Connection connection) {
        this.connection = connection;
    }

    public void load(File file, String tableName) throws SQLException, IOException {
        final int[] types = columnsTypes(tableName);

        final BufferedReader fileReader = readGZIPFile(file);

        final ProgressLogger logger = new ProgressLogger();

        final int numberOfRowsPerInsert = 10_000;

        try (final CSVParser parser = CSVFormat.DEFAULT.withDelimiter('|').parse(fileReader)) {
            logger.start();

            final Iterator<CSVRecord> records = parser.iterator();

            while (records.hasNext()) {
                try (final Statement statement = connection.createStatement()) {
                    statement.execute(createInsertQuery(tableName, records, types, numberOfRowsPerInsert));
                    logger.log(numberOfRowsPerInsert);
                }
            }
        }
    }

    private static BufferedReader readGZIPFile(File file) throws IOException {
        return new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(file))));
    }

    private int[] columnsTypes(String tableName) throws SQLException {
        final Statement statement = connection.createStatement();

        statement.execute("SELECT * FROM " + tableName);

        final ResultSetMetaData metaData = statement.getResultSet().getMetaData();

        final int numberOfColumns = metaData.getColumnCount();

        final int[] types = new int[numberOfColumns];

        for (int i = 0; i < numberOfColumns; i++) {
            types[i] = metaData.getColumnType(i + 1);
        }

        statement.close();

        return types;
    }

    private String createInsertQuery(String tableName, Iterator<CSVRecord> records, int[] types, int size) {
        final StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
        queryBuilder.append(tableName);
        queryBuilder.append(" VALUES");

        boolean commaSeparatorNeeded = false;

        int numberOfRows = 0;

        do {
            if (commaSeparatorNeeded) {
                queryBuilder.append(',');
            }

            buildRow(queryBuilder, records.next(), types);

            commaSeparatorNeeded = true;
            numberOfRows++;
        } while (records.hasNext() && numberOfRows < size);

        return queryBuilder.toString();
    }

    private void buildRow(StringBuilder queryBuilder, CSVRecord record, int[] types) {
        queryBuilder.append('(');

        boolean commaSeparatorNeeded = false;

        for (int j = 0; j < types.length; j++) {
            if (commaSeparatorNeeded) {
                queryBuilder.append(',');
            }

            switch (types[j]) {
                case Types.VARCHAR:
                    queryBuilder.append('\'');
                    queryBuilder.append(record.get(j));
                    queryBuilder.append('\'');
                    break;
                case Types.DATE:
                    queryBuilder.append("date '");
                    queryBuilder.append(record.get(j));
                    queryBuilder.append('\'');
                    break;
                default:
                    queryBuilder.append(record.get(j));
            }

            commaSeparatorNeeded = true;
        }

        queryBuilder.append(')');
    }
}
