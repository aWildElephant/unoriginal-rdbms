package fr.awildelephant.rdbms.lexer;

import fr.awildelephant.rdbms.lexer.tokens.Token;
import fr.awildelephant.rdbms.lexer.tokens.TokenType;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

final class LexingTestHelper {

    private LexingTestHelper() {

    }

    static void assertLexing(String input, Token... expected) {
        assertThat(tryLexing(input)).containsExactly(expected);
    }

    static ArrayList<Token> tryLexing(String input) {
        final Lexer lexer = lexerFrom(input);
        final ArrayList<Token> actualTokens = new ArrayList<>();

        while (lexer.lookupNextToken().type() != TokenType.END_OF_FILE) {
            actualTokens.add(lexer.consumeNextToken());
        }

        return actualTokens;
    }

    static Lexer lexerFrom(String input) {
        return new Lexer(InputStreamWrapper.wrap(input));
    }
}
