package fr.awildelephant.rdbms.client;

import fr.awildelephant.rdbms.server.RDBMS;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

public class RDBMSStatement extends AbstractStatement {

    private RDBMS system;
    private boolean isClosed;

    RDBMSStatement(RDBMS system) {
        this.system = system;
    }

    @Override
    public boolean execute(String sql) {
        return system.execute(sql);
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
