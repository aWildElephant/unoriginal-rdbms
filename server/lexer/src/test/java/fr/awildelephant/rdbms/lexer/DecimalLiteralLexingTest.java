package fr.awildelephant.rdbms.lexer;

import fr.awildelephant.rdbms.lexer.tokens.DecimalLiteralToken;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static fr.awildelephant.rdbms.lexer.LexingTestHelper.assertLexing;

class DecimalLiteralLexingTest {

    @Test
    void it_should_match_a_decimal_literal() {
        assertLexing("9.99", new DecimalLiteralToken(new BigDecimal("9.99")));
    }
}
