package fr.awildelephant.gitrdbms.lexer;

import fr.awildelephant.gitrdbms.lexer.tokens.IdentifierToken;
import fr.awildelephant.gitrdbms.lexer.tokens.IntegerLiteralToken;
import fr.awildelephant.gitrdbms.lexer.tokens.Token;
import fr.awildelephant.gitrdbms.lexer.tokens.TokenType;

import java.util.InputMismatchException;

import static fr.awildelephant.gitrdbms.lexer.tokens.StaticToken.*;

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

        if (firstCharacter >= '0' && firstCharacter <= '9') {
            return matchIntegerLiteral(firstCharacter);
        }

        switch (firstCharacter) {
            case 'c':
            case 'C':
                return matchC();
            case 'f':
            case 'F':
                return matchF();
            case 'i':
            case 'I':
                readAndExpect('n', 'N');

                return matchIN();
            case 's':
            case 'S':
                return matchS();
            case 't':
            case 'T':
                return matchT();
            case 'v':
            case 'V':
                return matchV();
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

    private Token matchIntegerLiteral(int firstCharacter) {
        return new IntegerLiteralToken(firstCharacter - '0');
    }

    private Token matchC() {
        readAndExpect('r', 'R');
        readAndExpect('e', 'E');
        readAndExpect('a', 'A');
        readAndExpect('t', 'T');
        readAndExpect('e', 'E');

        return CREATE_TOKEN;
    }

    private Token matchF() {
        readAndExpect('r', 'R');
        readAndExpect('o', 'O');
        readAndExpect('m', 'M');

        return FROM_TOKEN;
    }

    private Token matchIN() {
        final int thirdCharacter = read();

        switch (thirdCharacter) {
            case 's':
            case 'S':
                return matchINS();
            case 't':
            case 'T':
                return matchINT();
            default:
                throw new InputMismatchException();
        }
    }

    private Token matchINS() {
        readAndExpect('e', 'E');
        readAndExpect('r', 'R');
        readAndExpect('t', 'T');

        return INSERT_TOKEN;
    }

    private Token matchINT() {
        final int fourthCharacter = read();

        switch (fourthCharacter) {
            case 'e':
            case 'E':
                return matchINTE();
            case 'o':
            case 'O':
                return INTO_TOKEN;
            default:
                throw new InputMismatchException();
        }
    }

    private Token matchINTE() {
        readAndExpect('g', 'G');
        readAndExpect('e', 'E');
        readAndExpect('r', 'R');

        return INTEGER_TOKEN;
    }

    private Token matchS() {
        readAndExpect('e', 'E');
        readAndExpect('l', 'L');
        readAndExpect('e', 'E');
        readAndExpect('c', 'C');
        readAndExpect('t', 'T');

        return SELECT_TOKEN;
    }

    private Token matchT() {
        readAndExpect('a', 'A');
        readAndExpect('b', 'B');
        readAndExpect('l', 'L');
        readAndExpect('e', 'E');

        return TABLE_TOKEN;
    }

    private Token matchV() {
        readAndExpect('a', 'A');
        readAndExpect('l', 'L');
        readAndExpect('u', 'U');
        readAndExpect('e', 'E');
        readAndExpect('s', 'S');

        return VALUES_TOKEN;
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
