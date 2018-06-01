package fr.awildelephant.rdbms.jdbc;

import fr.awildelephant.rdbms.system.RDBMS;

import java.sql.ResultSet;

public class RDBMSStatement extends AbstractStatement {

    private RDBMS system;
    private boolean isClosed;

    RDBMSStatement(RDBMS system) {
        this.system = system;
    }

    @Override
    public boolean execute(String sql) {
        return false;
    }

    @Override
    public ResultSet executeQuery(String sql) {
        return null;
    }

    @Override
    public int executeUpdate(String sql) {
        return system.update(sql);
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
