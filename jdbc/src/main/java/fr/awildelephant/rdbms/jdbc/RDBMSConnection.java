package fr.awildelephant.rdbms.jdbc;

import fr.awildelephant.rdbms.system.RDBMS;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

public class RDBMSConnection extends AbstractConnection {

    private List<RDBMSStatement> statements = new LinkedList<>();
    private RDBMS system;
    private boolean isClosed;

    RDBMSConnection(RDBMS system) {
        this.system = system;
    }

    @Override
    public RDBMSStatement createStatement() {
        final RDBMSStatement statement = new RDBMSStatement(system);

        statements.add(statement);

        return statement;
    }

    @Override
    public void close() {
        if (!isClosed) {
            statements.forEach(RDBMSStatement::close);

            statements = null;
            system = null;
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
