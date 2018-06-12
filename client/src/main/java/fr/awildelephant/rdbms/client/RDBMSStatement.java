package fr.awildelephant.rdbms.client;

import fr.awildelephant.rdbms.engine.Table;
import fr.awildelephant.rdbms.server.RDBMS;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

public class RDBMSStatement extends AbstractStatement {

    private RDBMS system;
    private boolean isClosed;
    private ResultSet lastResult;

    RDBMSStatement(RDBMS system) {
        this.system = system;
    }

    @Override
    public boolean execute(String sql) {
        final Table table = system.execute(sql);

        if (table == null) {
            lastResult = null;

            return false;
        }

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
            system = null;
            isClosed = true;
        }
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }
}
