package fr.awildelephant.rdbms.test.commons;

import fr.awildelephant.rdbms.embedded.Driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class RDBMSTestWrapper {

    private static final String CONNECTION_STRING = "jdbc:rdbms:mem:%s";

    private final Connection connection;

    private Statement lastStatement;
    private SQLException lastException;

    public RDBMSTestWrapper(String identifier) throws SQLException {
        connection = new Driver().connect(String.format(CONNECTION_STRING, identifier), null);
    }

    public void execute(String query) throws SQLException {
        forwardExceptionIfPresent();

        try {
            reset();

            final Statement statement = connection.createStatement();

            statement.execute(query);

            lastStatement = statement;
        } catch (SQLException e) {
            lastException = e;
        }
    }

    public void forwardExceptionIfPresent() throws SQLException {
        if (lastException != null) {
            throw lastException;
        }
    }

    public SQLException lastException() {
        return lastException;
    }

    public Statement getStatement() {
        return lastStatement;
    }

    private void reset() throws SQLException {
        if (lastStatement != null) {
            lastStatement.close();
            lastStatement = null;
        }
    }

    public Connection connection() {
        return connection;
    }
}
