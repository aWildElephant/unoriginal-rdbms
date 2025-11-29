package fr.awildelephant.rdbms.lexer;

import fr.awildelephant.rdbms.lexer.exception.LexingException;
import fr.awildelephant.rdbms.lexer.tokens.DecimalLiteralToken;
import fr.awildelephant.rdbms.lexer.tokens.IdentifierToken;
import fr.awildelephant.rdbms.lexer.tokens.IntegerLiteralToken;
import fr.awildelephant.rdbms.lexer.tokens.Keywords;
import fr.awildelephant.rdbms.lexer.tokens.TextLiteralToken;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.ASTERISK_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.COMMA_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.END_OF_FILE_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.EQUAL_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.GREATER_OR_EQUAL_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.GREATER_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.LEFT_PAREN_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.LESS_OR_EQUAL_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.LESS_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.MINUS_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.NOT_EQUAL_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.PERIOD_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.PLUS_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.QUESTION_MARK_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.RIGHT_PAREN_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.SEMICOLON_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.SOLIDUS_TOKEN;

public final class Lexer {

    private static final int END_OF_INPUT = -1;

    private final InputStreamWrapper input;
    private final List<Token> readTokens;

    private int cursor;

    public Lexer(InputStreamWrapper input) {
        this.input = input;

        this.readTokens = new ArrayList<>();
    }

    public Token lookupNextToken() {
        // cursor is at most readTokens.size() + 1
        if (cursor >= readTokens.size()) {
            readTokens.add(matchNextToken());
        }

        return readTokens.get(cursor);
    }

    public Token consumeNextToken() {
        final Token token = lookupNextToken();

        cursor++;

        return token;
    }

    private Token matchNextToken() {
        int codePoint = input.get();

        while (isBlank(codePoint)) {
            input.next();

            codePoint = input.get();
        }

        if (codePoint < 0) {
            return END_OF_FILE_TOKEN;
        }

        input.next();

        return switch (codePoint) {
            case '*' -> ASTERISK_TOKEN;
            case '=' -> EQUAL_TOKEN;
            case '<' -> {
                if (input.get() == '=') {
                    input.next();

                    yield LESS_OR_EQUAL_TOKEN;
                } else if (input.get() == '>') {
                    input.next();

                    yield NOT_EQUAL_TOKEN;
                } else {
                    yield LESS_TOKEN;
                }
            }
            case '>' -> {
                if (input.get() == '=') {
                    input.next();

                    yield GREATER_OR_EQUAL_TOKEN;
                } else {
                    yield GREATER_TOKEN;
                }
            }
            case '/' -> SOLIDUS_TOKEN;
            case '+' -> PLUS_TOKEN;
            case '-' -> MINUS_TOKEN;
            case '(' -> LEFT_PAREN_TOKEN;
            case ')' -> RIGHT_PAREN_TOKEN;
            case ',' -> COMMA_TOKEN;
            case ';' -> SEMICOLON_TOKEN;
            case '?' -> QUESTION_MARK_TOKEN;
            case '.' -> PERIOD_TOKEN;
            case '\'' -> matchTextLiteral();
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> matchIntegerOrDecimalLiteral(codePoint);
            default -> matchKeywordOrIdentifier(codePoint);
        };
    }

    private Token matchTextLiteral() {
        final StringBuilder builder = new StringBuilder();

        while (true) {
            int codePoint = input.get();

            input.next();

            if (codePoint == END_OF_INPUT) {
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
            } else if (isEndOfNumber(codePoint)) {
                if (decimalSeparatorFound) {
                    return new DecimalLiteralToken(new BigDecimal(valueBuilder.toString()));
                } else {
                    return new IntegerLiteralToken(new BigInteger(valueBuilder.toString()));
                }
            } else {
                throw new LexingException();
            }

            input.next();
        }
    }

    private boolean isEndOfNumber(int codePoint) {
        return codePoint == END_OF_INPUT || codePoint == ',' || codePoint == ';' || codePoint == '(' || codePoint == ')' || isBlank(codePoint);
    }

    private boolean isBlank(int codePoint) {
        return codePoint == ' ' || codePoint == '\t' || codePoint == '\n';
    }

    private boolean isValidForIdentifier(int codePoint) {
        return (codePoint >= 'a' && codePoint <= 'z')
                || (codePoint >= 'A' && codePoint <= 'Z')
                || (codePoint >= '0' && codePoint <= '9')
                || codePoint == '_';
    }

    public LexerSnapshot save() {
        return new LexerSnapshot(cursor);
    }

    public void restore(LexerSnapshot snapshot) {
        cursor = snapshot.cursor();
    }
}
