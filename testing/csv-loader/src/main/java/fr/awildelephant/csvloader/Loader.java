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
import java.util.StringJoiner;
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

        try (final CSVParser parser = CSVFormat.DEFAULT.withDelimiter('|').parse(fileReader)) {

            logger.start();

            for (CSVRecord record : parser) {
                try (final Statement statement = connection.createStatement()) {
                    final String insertQuery = createInsertQuery(tableName, record, types);
                    statement.execute(insertQuery);
                    logger.log(1);
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

    private String createInsertQuery(String tableName, CSVRecord record, int[] types) {
        final StringJoiner queryBuilder = new StringJoiner(", ", "INSERT INTO " + tableName + " VALUES (", ")");

        for (int i = 0; i < types.length; i++) {
            switch (types[i]) {
                case Types.VARCHAR:
                    queryBuilder.add("'" + record.get(i) + "'");
                    break;
                case Types.DATE:
                    queryBuilder.add("date '" + record.get(i) + "'");
                    break;
                default:
                    queryBuilder.add(record.get(i));
            }
        }

        return queryBuilder.toString();
    }
}
