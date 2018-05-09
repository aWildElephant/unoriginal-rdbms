package fr.awildelephant.gitrdbms.lexer.tokens;

import org.junit.jupiter.api.Test;

import static fr.awildelephant.gitrdbms.lexer.tokens.LexingTestHelper.assertLexing;

class IdentifierLexingTest {

    @Test
    void it_should_match_a_single_letter_as_an_identifier() {
        assertLexing("a", new IdentifierToken("a"));
    }

    @Test
    void it_should_properly_match_an_identifier_with_multiple_letters() {
        assertLexing("abc", new IdentifierToken("abc"));
    }
}
