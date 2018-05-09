package fr.awildelephant.gitrdbms.lexer.tokens;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;

import static fr.awildelephant.gitrdbms.lexer.tokens.LexingTestHelper.assertLexing;
import static fr.awildelephant.gitrdbms.lexer.tokens.StaticToken.END_OF_FILE_TOKEN;

class StaticTokenLexingTest {

    private static Object[][] staticTokensParams() {
        return Arrays.stream(StaticToken.values())
                .filter(token -> token != END_OF_FILE_TOKEN)
                .map(token -> new Object[]{token.text(), token})
                .toArray(Object[][]::new);
    }

    @ParameterizedTest
    @MethodSource("staticTokensParams")
    void it_should_match_a_single_static_token(String inputText, Token expectedToken) {
        assertLexing(inputText, expectedToken);
    }

}
