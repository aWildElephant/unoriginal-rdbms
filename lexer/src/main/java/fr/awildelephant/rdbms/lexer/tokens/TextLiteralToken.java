package fr.awildelephant.rdbms.lexer.tokens;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.TEXT_LITERAL;

public final class TextLiteralToken implements Token {

    private final String content;

    public TextLiteralToken(String content) {
        this.content = content;
    }

    public String content() {
        return content;
    }

    @Override
    public String text() {
        return "<text literal>";
    }

    @Override
    public TokenType type() {
        return TEXT_LITERAL;
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TextLiteralToken)) {
            return false;
        }

        final TextLiteralToken other = (TextLiteralToken) obj;

        return content.equals(other.content);
    }

    @Override
    public String toString() {
        return "TEXT_TOKEN(" + content + ")";
    }
}
