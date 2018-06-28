package fr.awildelephant.rdbms.lexer;

import fr.awildelephant.rdbms.lexer.tokens.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.InputMismatchException;

import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.*;
import static java.lang.Integer.parseInt;

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
                return matchIntegerOrDecimalLiteral(codePoint);
            default:
                return matchKeywordOrIdentifier(codePoint); // TODO: check that first codePoint is allowed for an identifier
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

    private Token matchIntegerOrDecimalLiteral(int firstCodePoint) {
        final StringBuilder valueBuilder = new StringBuilder().appendCodePoint(firstCodePoint);
        boolean decimalSeparatorFound = false;

        while (true) {
            final int codePoint = input.get();

            if (codePoint == '.') {
                if (decimalSeparatorFound) {
                    throw new InputMismatchException();
                }

                decimalSeparatorFound = true;
                valueBuilder.appendCodePoint(codePoint);
            } else if (codePoint >= '0' && codePoint <= '9') {
                valueBuilder.appendCodePoint(codePoint);
            } else {
                if (decimalSeparatorFound) {
                    return new DecimalLiteralToken(new BigDecimal(valueBuilder.toString()));
                } else {
                    return new IntegerLiteralToken(parseInt(valueBuilder.toString()));
                }
            }

            input.next();
        }
    }

    private boolean isBlank(int codePoint) {
        return codePoint == ' ' || codePoint == '\t' || codePoint == '\n';
    }

    private boolean isValidForIdentifier(int codePoint) {
        return (codePoint >= 'a' && codePoint <= 'z') || (codePoint >= 'A' && codePoint <= 'Z');
    }
}
