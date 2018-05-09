package fr.awildelephant.gitrdbms.lexer;

import fr.awildelephant.gitrdbms.lexer.tokens.IdentifierToken;
import fr.awildelephant.gitrdbms.lexer.tokens.Token;

import java.util.InputMismatchException;

import static fr.awildelephant.gitrdbms.lexer.tokens.StaticToken.COMMA_TOKEN;
import static fr.awildelephant.gitrdbms.lexer.tokens.StaticToken.CREATE_TOKEN;
import static fr.awildelephant.gitrdbms.lexer.tokens.StaticToken.END_OF_FILE_TOKEN;
import static fr.awildelephant.gitrdbms.lexer.tokens.StaticToken.INTEGER_TOKEN;
import static fr.awildelephant.gitrdbms.lexer.tokens.StaticToken.LEFT_PAREN_TOKEN;
import static fr.awildelephant.gitrdbms.lexer.tokens.StaticToken.RIGHT_PAREN_TOKEN;
import static fr.awildelephant.gitrdbms.lexer.tokens.StaticToken.SEMICOLON_TOKEN;
import static fr.awildelephant.gitrdbms.lexer.tokens.StaticToken.TABLE_TOKEN;

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

        while (isBlank(input.get())) {
            input.next();
        }

        final int firstCharacter = read();

        switch (firstCharacter) {
            case 'c':
            case 'C':
                return matchCreate();
            case 't':
            case 'T':
                return matchTable();
            case 'i':
            case 'I':
                return matchInteger();
            case '(':
                return LEFT_PAREN_TOKEN;
            case ')':
                return RIGHT_PAREN_TOKEN;
            case ',':
                return COMMA_TOKEN;
            case ';':
                return SEMICOLON_TOKEN;
            default:
                // TODO: https://www.youtube.com/watch?v=07So_lJQyqw, allow matching identifiers starting with c/t/i
                return matchIdentifier(firstCharacter);
        }
    }

    private boolean isBlank(int codePoint) {
        return codePoint == ' ' || codePoint == '\t' || codePoint == '\n';
    }

    private Token matchIdentifier(int firstCharacter) {
        final StringBuilder builder = new StringBuilder();

        if (isValidForIdentifier(firstCharacter)) {
            builder.appendCodePoint(firstCharacter);
        }

        while (isValidForIdentifier(input.get())) {
            builder.appendCodePoint(input.get());

            input.next();
        }

        return new IdentifierToken(builder.toString());
    }

    private boolean isValidForIdentifier(int character) {
        return (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z');
    }

    private Token matchInteger() {
        readAndExpect('n', 'N');
        readAndExpect('t', 'T');
        readAndExpect('e', 'E');
        readAndExpect('g', 'G');
        readAndExpect('e', 'E');
        readAndExpect('r', 'R');

        return INTEGER_TOKEN;
    }

    private Token matchTable() {
        readAndExpect('a', 'A');
        readAndExpect('b', 'B');
        readAndExpect('l', 'L');
        readAndExpect('e', 'E');

        return TABLE_TOKEN;
    }

    private Token matchCreate() {
        readAndExpect('r', 'R');
        readAndExpect('e', 'E');
        readAndExpect('a', 'A');
        readAndExpect('t', 'T');
        readAndExpect('e', 'E');

        return CREATE_TOKEN;
    }

    private void readAndExpect(char lowercase, char uppercase) {
        final int actual = read();

        if (actual != lowercase && actual != uppercase) {
            throw new InputMismatchException();
        }
    }

    private int read() {
        final int codePoint = input.get();

        input.next();

        return codePoint;
    }
}
