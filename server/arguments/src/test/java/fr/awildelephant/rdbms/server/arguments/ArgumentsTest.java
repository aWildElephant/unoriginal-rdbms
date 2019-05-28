package fr.awildelephant.rdbms.server.arguments;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class ArgumentsTest {

    @Test
    void it_should_implement_equals_and_hashCode() {
        EqualsVerifier.forClass(Arguments.class).verify();
    }
}
