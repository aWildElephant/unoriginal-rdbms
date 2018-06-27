package fr.awildelephant.rdbms.lexer;

import fr.awildelephant.rdbms.lexer.tokens.*;

import java.util.Arrays;
import java.util.InputMismatchException;

import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.*;

public final class Lexer {

    private final InputStreamWrapper input;

    private Token nextToken;

    public Lexer(InputStreamWrapper input) {
        this.input = input;
    }

    public Token lookupNextToken() {
        if (nextToken == null) {
            nextToken = matchNextToken();
        }

        return nextToken;
    }

    public Token consumeNextToken() {
        if (nextToken != null) {
            final Token token = nextToken;
            nextToken = null;

            return token;
        }

        return matchNextToken();
    }

    private Token matchNextToken() {
        if (input.isFinished()) {
            return END_OF_FILE_TOKEN;
        }

        int codePoint = input.get();

        while (isBlank(codePoint)) {
            input.next();

            codePoint = input.get();
        }

        input.next();

        switch (codePoint) {
            case '*':
                return ASTERISK_TOKEN;
            case '(':
                return LEFT_PAREN_TOKEN;
            case ')':
                return RIGHT_PAREN_TOKEN;
            case ',':
                return COMMA_TOKEN;
            case ';':
                return SEMICOLON_TOKEN;
            case '\'':
                return matchTextLiteral();
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return matchIntegerLiteral(codePoint);
            default:
                return matchKeywordOrIdentifier(codePoint);
        }
    }

    private Token matchTextLiteral() {
        final StringBuilder builder = new StringBuilder();

        while (true) {
            int codePoint = input.get();

            input.next();

            if (codePoint == -1) {
                throw new InputMismatchException();
            } else if (codePoint == '\'') {
                if (input.get() == '\'') {
                    input.next();
                } else {
                    return new TextLiteralToken(builder.toString());
                }
            }

            builder.appendCodePoint(codePoint);
        }
    }

    private Token matchKeywordOrIdentifier(int firstCodePoint) {
        final StringBuilder builder = new StringBuilder();

        builder.appendCodePoint(firstCodePoint);

        while (isValidForIdentifier(input.get())) {
            builder.appendCodePoint(input.get());

            input.next();
        }

        final String obtained = builder.toString().toLowerCase();

        return Arrays.<Token>stream(Keywords.values())
                .filter(keyword -> keyword.text().equals(obtained))
                .findAny()
                .orElse(new IdentifierToken(obtained));
    }

    private Token matchIntegerLiteral(int firstCodePoint) {
        int value = intValueOf(firstCodePoint);

        int next = input.get();
        while (next >= '0' && next <= '9') {
            value = value * 10 + intValueOf(next);
            input.next();
            next = input.get();
        }

        return new IntegerLiteralToken(value);
    }

    private int intValueOf(int firstCodePoint) {
        return firstCodePoint - '0';
    }

    private boolean isBlank(int codePoint) {
        return codePoint == ' ' || codePoint == '\t' || codePoint == '\n';
    }

    private boolean isValidForIdentifier(int codePoint) {
        return (codePoint >= 'a' && codePoint <= 'z') || (codePoint >= 'A' && codePoint <= 'Z');
    }
}
