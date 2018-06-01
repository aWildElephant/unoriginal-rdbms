package fr.awildelephant.rdbms.lexer;

import fr.awildelephant.rdbms.lexer.tokens.Token;
import fr.awildelephant.rdbms.lexer.tokens.TokenType;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

final class LexingTestHelper {

    private LexingTestHelper() {

    }

    static void assertLexing(String input, Token... expected) {
        final Lexer lexer = new Lexer(InputStreamWrapper.wrap(input));
        final ArrayList<Token> actualTokens = new ArrayList<>(expected.length);

        while (lexer.lookupNextToken().type() != TokenType.END_OF_FILE) {
            actualTokens.add(lexer.consumeNextToken());
        }

        assertThat(actualTokens).containsExactly(expected);
    }
}
