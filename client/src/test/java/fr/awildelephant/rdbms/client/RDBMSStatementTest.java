package fr.awildelephant.rdbms.client;

import fr.awildelephant.rdbms.server.RDBMS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RDBMSStatementTest {

    private RDBMSStatement statement;

    @BeforeEach
    void setUp() {
        statement = new RDBMSStatement(new RDBMS());
    }

    @Test
    void it_should_not_be_closed_when_created() {
        assertFalse(statement.isClosed());
    }

    @Test
    void it_should_be_closed_after_having_been_closed() {
        statement.close();

        assertTrue(statement.isClosed());
    }
}