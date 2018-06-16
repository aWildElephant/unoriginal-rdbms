package fr.awildelephant.rdbms.lexer;

import fr.awildelephant.rdbms.lexer.tokens.IdentifierToken;
import fr.awildelephant.rdbms.lexer.tokens.IntegerLiteralToken;
import fr.awildelephant.rdbms.lexer.tokens.Token;

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
            case '(':
                return LEFT_PAREN_TOKEN;
            case ')':
                return RIGHT_PAREN_TOKEN;
            case ',':
                return COMMA_TOKEN;
            case ';':
                return SEMICOLON_TOKEN;
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

    private boolean isBlank(int codePoint) {
        return codePoint == ' ' || codePoint == '\t' || codePoint == '\n';
    }

    private Token matchKeywordOrIdentifier(int firstCodePoint) {
        final StringBuilder builder = new StringBuilder();

        builder.appendCodePoint(firstCodePoint);

        while (isValidForIdentifier(input.get())) {
            builder.appendCodePoint(input.get());

            input.next();
        }

        final String obtained = builder.toString().toLowerCase();

        switch (obtained) {
            case "create":
                return CREATE_TOKEN;
            case "from":
                return FROM_TOKEN;
            case "insert":
                return INSERT_TOKEN;
            case "integer":
                return INTEGER_TOKEN;
            case "into":
                return INTO_TOKEN;
            case "select":
                return SELECT_TOKEN;
            case "table":
                return TABLE_TOKEN;
            case "values":
                return VALUES_TOKEN;
            default:
                return new IdentifierToken(obtained);
        }
    }

    private Token matchIntegerLiteral(int firstCodePoint) {
        return new IntegerLiteralToken(firstCodePoint - '0');
    }

    private boolean isValidForIdentifier(int codePoint) {
        return (codePoint >= 'a' && codePoint <= 'z') || (codePoint >= 'A' && codePoint <= 'Z');
    }
}
