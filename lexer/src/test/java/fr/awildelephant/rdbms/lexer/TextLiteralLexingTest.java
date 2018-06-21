package fr.awildelephant.rdbms.lexer;

import fr.awildelephant.rdbms.lexer.tokens.TextLiteralToken;
import org.junit.jupiter.api.Test;

import java.util.InputMismatchException;

import static fr.awildelephant.rdbms.lexer.LexingTestHelper.assertLexing;
import static fr.awildelephant.rdbms.lexer.LexingTestHelper.tryLexing;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TextLiteralLexingTest {

    @Test
    void it_should_match_a_text_literal() {
        assertLexing("'bonjour'", new TextLiteralToken("bonjour"));
    }

    @Test
    void it_should_throw_an_exception_if_the_quote_is_not_closed() {
        assertThrows(InputMismatchException.class, () -> tryLexing("'bon"));
    }

    @Test
    void it_should_escape_a_quote_with_a_quote() {
        assertLexing("'here is a quote: '''", new TextLiteralToken("here is a quote: '"));
    }
}
