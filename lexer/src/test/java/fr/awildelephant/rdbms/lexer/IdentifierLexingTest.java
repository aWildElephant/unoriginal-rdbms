package fr.awildelephant.rdbms.lexer;

import fr.awildelephant.rdbms.lexer.tokens.IdentifierToken;
import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.lexer.LexingTestHelper.assertLexing;

class IdentifierLexingTest {

    @Test
    void it_should_match_a_single_letter_as_an_identifier() {
        assertLexing("a", new IdentifierToken("a"));
    }

    @Test
    void it_should_match_an_identifier_with_multiple_letters() {
        assertLexing("abc", new IdentifierToken("abc"));
    }

    @Test
    void it_should_match_an_identifier_that_starts_with_the_same_letter_as_some_reserved_keyword() {
        assertLexing("toto", new IdentifierToken("toto"));
    }
}
