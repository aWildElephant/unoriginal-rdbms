package fr.awildelephant.rdbms.driver;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DriverTest {

    @Test
    void it_should_accept_an_url_for_a_local_server() throws SQLException {
        assertTrue(new Driver().acceptsURL("jdbc:rdbms://localhost:1234/caca"));
    }
}