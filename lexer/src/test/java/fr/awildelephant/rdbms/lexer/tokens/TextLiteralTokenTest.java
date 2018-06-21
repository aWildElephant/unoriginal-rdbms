package fr.awildelephant.rdbms.lexer.tokens;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TextLiteralTokenTest {

    @Test
    void it_should_implement_equals_and_hashCode() {
        EqualsVerifier.forClass(TextLiteralToken.class).withNonnullFields("content").verify();
    }

    @Test
    void toString_should_contain_the_content_of_the_token() {
        final String content = "bonsoir";

        final TextLiteralToken text = new TextLiteralToken(content);

        assertThat(text.toString()).contains(content);
    }
}
