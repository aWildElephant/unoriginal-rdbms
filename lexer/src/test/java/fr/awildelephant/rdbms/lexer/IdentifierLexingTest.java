package fr.awildelephant.rdbms.lexer;

import fr.awildelephant.rdbms.lexer.tokens.IdentifierToken;
import org.junit.jupiter.api.Test;

class IdentifierLexingTest {

    @Test
    void it_should_match_a_single_letter_as_an_identifier() {
        LexingTestHelper.assertLexing("a", new IdentifierToken("a"));
    }

    @Test
    void it_should_properly_match_an_identifier_with_multiple_letters() {
        LexingTestHelper.assertLexing("abc", new IdentifierToken("abc"));
    }
}
