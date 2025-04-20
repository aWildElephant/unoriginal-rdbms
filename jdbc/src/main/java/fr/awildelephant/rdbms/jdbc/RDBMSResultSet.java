package fr.awildelephant.rdbms.jdbc;

import fr.awildelephant.rdbms.jdbc.abstraction.ResultProxy;
import fr.awildelephant.rdbms.jdbc.abstraction.Value;
import fr.awildelephant.rdbms.jdbc.exception.ResourceClosedSQLException;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;

public class RDBMSResultSet extends AbstractResultSet {

    private ResultProxy table;
    private boolean isClosed;
    private boolean wasNull;

    private int cursor = -1;

    RDBMSResultSet(ResultProxy table) {
        this.table = table;
    }

    //region getters
    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        final Value value = field(columnIndex);

        if (value.isNull()) {
            wasNull = true;

            return false;
        }

        return value.getBoolean();
    }

    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {
        return getBoolean(table.position(columnLabel) + 1);
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        final Value value = field(columnIndex);

        if (value.isNull()) {
            wasNull = true;

            return null;
        }

        return value.getBigDecimal();
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return getBigDecimal(table.position(columnLabel) + 1);
    }

    @Override
    public Date getDate(int columnIndex) throws SQLException {
        final Value value = field(columnIndex);

        if (value.isNull()) {
            wasNull = true;
            return null;
        }

        return value.getDate();
    }

    // TODO: implement getDate methods with Calendar as second parameter?
    // TODO: NullPointerException if the ResultSet is closed
    @Override
    public int getInt(int columnIndex) throws SQLException {
        final Value value = field(columnIndex);

        if (value.isNull()) {
            wasNull = true;
            return 0;
        }

        return value.getInt();
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        return getInt(table.position(columnLabel) + 1);
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        final Value value = field(columnIndex);

        if (value.isNull()) {
            wasNull = true;
            return 0;
        }

        return value.getLong();
    }

    @Override
    public long getLong(String columnLabel) throws SQLException {
        return getLong(table.position(columnLabel) - 1);
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
        final Value value = field(columnIndex);

        if (value.isNull()) {
            wasNull = true;

            return null;
        }

        return value.getString();
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        return getString(table.position(columnLabel) + 1);
    }

    @Override
    public int getRow() {
        return cursor + 1;
    }

    @Override
    public boolean wasNull() {
        return wasNull;
    }
    //endregion

    //region navigation
    @Override
    public boolean next() {
        if (cursor < table.numberOfRows() - 1) {
            wasNull = false;
            cursor++;

            return true;
        }

        return false;
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        checkNotClosed();
        return cursor == -1;
    }

    @Override
    public boolean isFirst() throws SQLException {
        checkNotClosed();
        return cursor == 0;
    }

    @Override
    public void beforeFirst() throws SQLException {
        checkNotClosed();
        cursor = -1;
    }

    @Override
    public boolean first() throws SQLException {
        checkNotClosed();
        cursor = 0;
        return true; // TODO: check the doc, what should we return + should we throw if result set is closed?
    }
    //endregion

    @Override
    public ResultSetMetaData getMetaData() {
        return new RDBMSResultSetMetaData(table);
    }

    @Override
    public SQLWarning getWarnings() {
        return null; // TODO: assuming that null must be returned if there is no warning
    }

    @Override
    public void clearWarnings() {
        // NOP: getWarnings not actually supported
    }

    @Override
    public void close() {
        isClosed = true;
        table = null;
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }

    private void checkNotClosed() throws ResourceClosedSQLException {
        if (isClosed()) {
            throw new ResourceClosedSQLException();
        }
    }

    private Value field(int columnIndex) throws SQLException {
        if (isClosed) {
            throw new SQLException("Result set is closed");
        }

        if (cursor < 0) {
            throw new SQLException("Cursor is not pointing to a valid row. Did you call next?");
        }

        return table.get(cursor, columnIndex - 1);
    }
}
