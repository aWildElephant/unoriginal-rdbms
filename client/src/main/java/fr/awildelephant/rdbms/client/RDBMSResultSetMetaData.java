package fr.awildelephant.rdbms.client;

import fr.awildelephant.rdbms.schema.Schema;

import java.sql.SQLException;
import java.sql.Types;

class RDBMSResultSetMetaData extends AbstractResultSetMetaData {

    private final Schema schema;

    RDBMSResultSetMetaData(Schema schema) {
        this.schema = schema;
    }

    @Override
    public int getColumnCount() {
        return schema.numberOfAttributes();
    }

    @Override
    public String getColumnName(int i) {
        return schema.columnNames().get(i - 1);
    }

    @Override
    public String getColumnLabel(int i) {
        return getColumnName(i);
    }

    @Override
    public int isNullable(int i) {
        return schema.column(getColumnName(i)).notNull() ? columnNoNulls : columnNullable;
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
        return schema.column(getColumnName(i)).domain().name();
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
        switch (domainName) {
            case "DATE":
                return Types.DATE;
            case "DECIMAL":
                return Types.DECIMAL;
            case "INTEGER":
                return Types.INTEGER;
            case "TEXT":
                return Types.VARCHAR; // TODO: probably not exactly what we want
            default:
                throw new SQLException("Unknown domain " + domainName);
        }
    }
}
