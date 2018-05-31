package fr.awildelephant.rdbms.jdbc;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

public class RDBMSConnection extends AbstractConnection {

    private final List<RDBMSStatement> statements = new LinkedList<>();

    private boolean isClosed;

    @Override
    public RDBMSStatement createStatement() {
        final RDBMSStatement statement = new RDBMSStatement();

        statements.add(statement);

        return statement;
    }

    @Override
    public void close() {
        if (!isClosed) {
            statements.forEach(RDBMSStatement::close);

            isClosed = true;
        }
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }

    @Override
    public int getTransactionIsolation() {
        return Connection.TRANSACTION_NONE;
    }
}
