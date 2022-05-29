package fr.awildelephant.rdbms.util.printer;

import fr.awildelephant.rdbms.util.printer.table.Row;
import fr.awildelephant.rdbms.util.printer.table.TableBuilder;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResultSetPrinter {

    private final GenericPrinter genericPrinter = new GenericPrinter();

    public String print(ResultSet resultSet) throws SQLException {
        return print(resultSet, Integer.MAX_VALUE);
    }

    public String print(ResultSet resultSet, int limit) throws SQLException {
        final ResultSetMetaData metaData = resultSet.getMetaData();

        final TableBuilder builder = TableBuilder.builder();

        final int columnCount = metaData.getColumnCount();

        final List<String> columnNames = new ArrayList<>(columnCount);
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            columnNames.add(metaData.getColumnName(columnIndex));
        }

        builder.addHeaderRow(new Row(columnNames));

        final List<String> columnTypes = new ArrayList<>(columnCount);
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            columnTypes.add(getColumnTypeLabel(metaData.getColumnType(columnIndex)));
        }

        builder.addHeaderRow(new Row(columnTypes));

        int count = 0;
        while (resultSet.next() && count < limit) {
            final List<String> row = new ArrayList<>(columnCount);
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                row.add(Objects.toString(resultSet.getObject(columnIndex)));
            }
            builder.addRow(new Row(row));
        }

        return genericPrinter.print(builder.build());
    }

    private String getColumnTypeLabel(int columnType) {
        return switch (columnType) {
            case Types.BIT -> "BIT";
            case Types.TINYINT -> "TINYINT";
            case Types.SMALLINT -> "SMALLINT";
            case Types.INTEGER -> "INTEGER";
            case Types.BIGINT -> "BIGINT";
            case Types.FLOAT -> "FLOAT";
            case Types.REAL -> "REAL";
            case Types.DOUBLE -> "DOUBLE";
            case Types.NUMERIC -> "NUMERIC";
            case Types.DECIMAL -> "DECIMAL";
            case Types.CHAR -> "CHAR";
            case Types.VARCHAR -> "VARCHAR";
            case Types.LONGVARCHAR -> "LONGVARCHAR";
            case Types.DATE -> "DATE";
            case Types.TIME -> "TIME";
            case Types.TIMESTAMP -> "TIMESTAMP";
            case Types.BINARY -> "BINARY";
            case Types.VARBINARY -> "VARBINARY";
            case Types.LONGVARBINARY -> "LONGVARBINARY";
            case Types.NULL -> "NULL";
            case Types.OTHER -> "OTHER";
            case Types.JAVA_OBJECT -> "JAVA_OBJECT";
            case Types.DISTINCT -> "DISTINCT";
            case Types.STRUCT -> "STRUCT";
            case Types.ARRAY -> "ARRAY";
            case Types.BLOB -> "BLOB";
            case Types.CLOB -> "CLOB";
            case Types.REF -> "REF";
            case Types.DATALINK -> "DATALINK";
            case Types.BOOLEAN -> "BOOLEAN";
            case Types.ROWID -> "ROWID";
            case Types.NCHAR -> "NCHAR";
            case Types.NVARCHAR -> "NVARCHAR";
            case Types.LONGNVARCHAR -> "LONGNVARCHAR";
            case Types.NCLOB -> "NCLOB";
            case Types.SQLXML -> "SQLXML";
            case Types.REF_CURSOR -> "REF_CURSOR";
            case Types.TIME_WITH_TIMEZONE -> "TIME_WITH_TIMEZONE";
            case Types.TIMESTAMP_WITH_TIMEZONE -> "TIMESTAMP_WITH_TIMEZONE";
            default -> "Unknown type '" + columnType + '\'';
        };
    }
}
