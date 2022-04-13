package fr.awildelephant.rdbms.lexer.tokens;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class DecimalLiteralTokenTest {

    @Test
    void it_should_implement_equals_and_hashCode() {
        EqualsVerifier.forClass(DecimalLiteralToken.class).suppress(Warning.BIGDECIMAL_EQUALITY).verify();
    }
}
