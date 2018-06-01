package fr.awildelephant.rdbms.lexer;

import fr.awildelephant.rdbms.lexer.tokens.IntegerLiteralToken;
import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.lexer.LexingTestHelper.assertLexing;

class IntegerLiteralLexingTest {

    @Test
    void it_should_match_a_number_lower_than_10() {
        assertLexing("5", new IntegerLiteralToken(5));
    }
}
