package fr.awildelephant.rdbms.jdbc;

import fr.awildelephant.rdbms.system.RDBMS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RDBMSConnectionTest {

    private RDBMSConnection connection;

    @BeforeEach
    void setUp() {
        connection = new RDBMSConnection(new RDBMS());
    }

    @Test
    void it_should_not_be_closed_when_created() {
        assertFalse(connection.isClosed());
    }

    @Test
    void it_should_be_closed_after_having_been_closed() {
        connection.close();

        assertTrue(connection.isClosed());
    }

    @Test
    void close_should_also_close_any_created_statement() {
        final RDBMSStatement statement = connection.createStatement();

        connection.close();

        assertTrue(statement.isClosed());
    }
}