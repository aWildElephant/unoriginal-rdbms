package fr.awildelephant.rdbms.lexer;

import fr.awildelephant.rdbms.lexer.tokens.IntegerLiteralToken;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static fr.awildelephant.rdbms.lexer.LexingTestHelper.assertLexing;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.MINUS_TOKEN;

class IntegerLiteralLexingTest {

    @Test
    void it_should_match_a_number_lower_than_10() {
        assertLexing("5", new IntegerLiteralToken(BigInteger.valueOf(5)));
    }

    @Test
    void it_should_match_a_number_greater_than_10() {
        assertLexing("2018", new IntegerLiteralToken(BigInteger.valueOf(2018)));
    }

    @Test
    void it_should_match_a_negative_value() {
        assertLexing("-1", MINUS_TOKEN, new IntegerLiteralToken(BigInteger.valueOf(1)));
    }

    @Test
    void it_should_match_a_value_greater_than_integer_max_value() {
        assertLexing("2147483648", new IntegerLiteralToken(BigInteger.valueOf(2147483648L)));
    }
}
