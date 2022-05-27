package fr.awildelephant.rdbms.jdbc;

import fr.awildelephant.rdbms.jdbc.abstraction.ResultProxy;
import fr.awildelephant.rdbms.jdbc.abstraction.ServerProxy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

public class RDBMSStatement extends AbstractStatement {

    private ServerProxy serverProxy;
    private boolean isClosed;
    private RDBMSResultSet lastResult;

    RDBMSStatement(ServerProxy serverProxy) {
        this.serverProxy = serverProxy;
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        final ResultProxy table = executeAndForwardException(sql);

        if (table == null) {
            lastResult = null;

            return false;
        }

        closeLastResultSet();

        lastResult = new RDBMSResultSet(table);

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
            serverProxy = null;
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

    private ResultProxy executeAndForwardException(String sql) throws SQLException {
        try {
            return serverProxy.execute(sql);
        } catch (Throwable e) {
            throw new SQLException(e.getMessage(), e);
        }
    }
}
