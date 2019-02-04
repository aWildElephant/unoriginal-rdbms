package fr.awildelephant.rdbms.lexer.tokens;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class DecimalLiteralTokenTest {

    @Test
    void it_should_implement_equals_and_hashCode() {
        EqualsVerifier.forClass(DecimalLiteralToken.class).verify();
    }
}
