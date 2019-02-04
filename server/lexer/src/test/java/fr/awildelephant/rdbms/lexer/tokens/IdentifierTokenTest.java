package fr.awildelephant.rdbms.lexer.tokens;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdentifierTokenTest {

    @Test
    void it_should_implement_equals_and_hashCode() {
        EqualsVerifier.forClass(IdentifierToken.class).withNonnullFields("text").verify();
    }

    @Test
    void toString_should_contain_the_text_representation_of_the_token() {
        final String textRepresentation = "theTextRepresentation";

        final IdentifierToken identifier = new IdentifierToken(textRepresentation);

        assertThat(identifier.toString()).contains(textRepresentation);
    }
}
