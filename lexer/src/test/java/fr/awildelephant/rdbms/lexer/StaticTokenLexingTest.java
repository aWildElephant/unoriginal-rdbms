package fr.awildelephant.rdbms.lexer;

import fr.awildelephant.rdbms.lexer.tokens.StaticToken;
import fr.awildelephant.rdbms.lexer.tokens.Token;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;

class StaticTokenLexingTest {

    private static Object[][] staticTokensParams() {
        return Arrays.stream(StaticToken.values())
                .filter(token -> token != StaticToken.END_OF_FILE_TOKEN)
                .map(token -> new Object[]{token.text(), token})
                .toArray(Object[][]::new);
    }

    @ParameterizedTest
    @MethodSource("staticTokensParams")
    void it_should_match_a_single_static_token(String inputText, Token expectedToken) {
        LexingTestHelper.assertLexing(inputText, expectedToken);
    }

}
