package fr.awildelephant.gitrdbms.lexer;

import fr.awildelephant.gitrdbms.lexer.tokens.Token;

import java.util.ArrayList;

import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.END_OF_FILE;
import static org.assertj.core.api.Assertions.assertThat;

final class LexingTestHelper {

    private LexingTestHelper() {

    }

    static void assertLexing(String input, Token... expected) {
        final Lexer lexer = new Lexer(InputStreamWrapper.wrap(input));
        final ArrayList<Token> actualTokens = new ArrayList<>(expected.length);

        while (lexer.lookupNextToken().type() != END_OF_FILE) {
            actualTokens.add(lexer.consumeNextToken());
        }

        assertThat(actualTokens).containsExactly(expected);
    }
}
