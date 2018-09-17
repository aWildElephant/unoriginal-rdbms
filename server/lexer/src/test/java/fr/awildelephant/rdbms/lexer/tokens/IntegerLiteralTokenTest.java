package fr.awildelephant.rdbms.lexer.tokens;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IntegerLiteralTokenTest {

    @Test
    void it_should_implement_equals_and_hashCode() {
        EqualsVerifier.forClass(IntegerLiteralToken.class).verify();
    }

    @Test
    void toString_should_contain_the_value_of_the_token() {
        final int value = 42;

        final IntegerLiteralToken integer = new IntegerLiteralToken(value);

        assertThat(integer.toString()).contains(String.valueOf(value));
    }
}