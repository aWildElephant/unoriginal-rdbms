package fr.awildelephant.rdbms.test.commons;

import fr.awildelephant.rdbms.embedded.Driver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RDBMSTestWrapper {

    private static final Logger LOGGER = LogManager.getLogger(RDBMSTestWrapper.class);
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
            LOGGER.error("Silencing exception", e);
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

    public ResultSet lastResultSet() throws SQLException {
        return lastStatement.getResultSet();
    }

    private void reset() throws SQLException {
        if (lastStatement != null) {
            lastStatement.close();
            lastStatement = null;
        }
    }
}
