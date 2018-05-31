package fr.awildelephant.rdbms.jdbc;

public class RDBMSStatement extends AbstractStatement {

    private boolean isClosed;

    @Override
    public boolean execute(String sql) {
        return false;
    }

    @Override
    public void close() {
        if (!isClosed) {
            isClosed = true;
        }
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }
}
