package fr.awildelephant.rdbms.jdbc;

import fr.awildelephant.rdbms.jdbc.abstraction.ServerProxy;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.LinkedList;
import java.util.List;

public class RDBMSConnection extends AbstractConnection {

    private List<RDBMSStatement> statements = new LinkedList<>();
    private ServerProxy serverProxy;
    private boolean isClosed;

    RDBMSConnection(ServerProxy serverProxy) {
        this.serverProxy = serverProxy;
    }

    @Override
    public RDBMSStatement createStatement() {
        final RDBMSStatement statement = new RDBMSStatement(serverProxy);

        statements.add(statement);

        return statement;
    }

    @Override
    public void close() {
        if (!isClosed) {
            statements.forEach(RDBMSStatement::close);

            statements = null;
            serverProxy = null;
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

    @Override
    public DatabaseMetaData getMetaData() {
        return RDBMSDatabaseMetaData.getMetaData();
    }
}
