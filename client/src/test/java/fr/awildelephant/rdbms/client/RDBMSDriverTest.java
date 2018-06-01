package fr.awildelephant.rdbms.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class RDBMSDriverTest {

    private RDBMSDriver driver;

    @BeforeEach
    void setUp() {
        driver = new RDBMSDriver();
    }

    @Test
    void acceptsURL_should_not_accept_this_girls_name_as_a_connection_string() throws SQLException {
        assertFalse(driver.acceptsURL("marion"));
    }

    @Test
    void acceptsURL_should_accept_any_connection_string_starting_with_the_magic_prefix() throws SQLException {
        assertTrue(driver.acceptsURL("client:rdbms:mem:idgaf_about_that_part"));
    }

    @Test
    void acceptsURL_should_throw_an_SQLException_if_the_url_is_null() {
        assertURLCannotBeNull(() -> driver.acceptsURL(null));
    }

    @Test
    void connect_should_throw_an_SQLException_if_the_url_is_a_talented_actor() {
        final SQLException exception = assertThrows(SQLException.class, () -> driver.connect("The rock", null));

        assertEquals(exception.getMessage(), "Invalid URL");
    }

    @Test
    void connect_should_throw_an_SQLException_if_the_url_is_null() {
        assertURLCannotBeNull(() -> driver.connect(null, null));
    }

    private void assertURLCannotBeNull(final Executable executable) {
        final SQLException exception = assertThrows(SQLException.class, executable);

        assertEquals(exception.getMessage(), "URL cannot be null");
    }
}