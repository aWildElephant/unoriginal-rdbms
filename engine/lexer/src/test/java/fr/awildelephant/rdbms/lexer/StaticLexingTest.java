package fr.awildelephant.rdbms.lexer;

import fr.awildelephant.rdbms.lexer.tokens.Keywords;
import fr.awildelephant.rdbms.lexer.tokens.StaticToken;
import fr.awildelephant.rdbms.lexer.tokens.Token;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static fr.awildelephant.rdbms.lexer.LexingTestHelper.assertLexing;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.END_OF_FILE_TOKEN;
import static java.util.Arrays.stream;

class StaticLexingTest {

    private static Object[][] staticTokensParams() {
        return Stream.<Token>concat(stream(Keywords.values()), stream(StaticToken.values()))
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
