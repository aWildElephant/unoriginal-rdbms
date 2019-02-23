package fr.awildelephant.rdbms.client;

import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.server.RDBMS;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

public class RDBMSStatement extends AbstractStatement {

    private RDBMS system;
    private boolean isClosed;
    private RDBMSResultSet lastResult;

    RDBMSStatement(RDBMS system) {
        this.system = system;
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        final Table table = executeAndForwardException(sql);

        if (table == null) {
            lastResult = null;

            return false;
        }

        closeLastResultSet();

        lastResult = new RDBMSResultSet(table.schema(), table.iterator());

        return true;
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public ResultSet getResultSet() {
        return lastResult;
    }

    @Override
    public void close() {
        if (!isClosed) {
            closeLastResultSet();

            lastResult = null;
            system = null;
            isClosed = true;
        }
    }

    private void closeLastResultSet() {
        if (lastResult != null) {
            lastResult.close();
        }
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }

    private Table executeAndForwardException(String sql) throws SQLException {
        try {
            return system.execute(sql);
        } catch (Throwable e) {
            throw new SQLException(e.getMessage(), e);
        }
    }
}
