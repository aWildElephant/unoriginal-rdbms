package fr.awildelephant.rdbms.jdbc;

import fr.awildelephant.rdbms.jdbc.abstraction.ResultProxy;

import java.sql.SQLException;
import java.sql.Types;

class RDBMSResultSetMetaData extends AbstractResultSetMetaData {

    private final ResultProxy table;

    RDBMSResultSetMetaData(ResultProxy table) {
        this.table = table;
    }

    @Override
    public int getColumnCount() {
        return table.numberOfColumns();
    }

    @Override
    public String getColumnName(int i) {
        return table.columnName(i - 1);
    }

    @Override
    public String getColumnLabel(int i) {
        return getColumnName(i);
    }

    @Override
    public int isNullable(int i) {
        return table.isNullable(i - 1) ? columnNullable : columnNoNulls;
    }

    @Override
    public int getPrecision(int i) {
        return 0; // TODO: guess here, check the doc
    }

    @Override
    public int getScale(int i) {
        return 0; // TODO: guess here as well
    }

    @Override
    public String getColumnTypeName(int i) {
        return table.columnTypeName(i - 1);
    }

    @Override
    public int getColumnDisplaySize(int i) {
        return 10; // TODO: completely arbitrary
    }

    @Override
    public int getColumnType(int i) throws SQLException {
        return getJDBCTypeFor(getColumnTypeName(i));
    }

    private int getJDBCTypeFor(String domainName) throws SQLException {
        return switch (domainName) {
            case "DATE" -> Types.DATE;
            case "DECIMAL" -> Types.DECIMAL;
            case "INTEGER" -> Types.INTEGER;
            case "TEXT" -> Types.VARCHAR; // TODO: probably not exactly what we want
            default -> throw new SQLException("Unknown domain " + domainName);
        };
    }
}
