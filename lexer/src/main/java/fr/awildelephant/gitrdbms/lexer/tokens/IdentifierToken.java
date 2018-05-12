package fr.awildelephant.gitrdbms.lexer.tokens;

import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.IDENTIFIER;

public final class IdentifierToken implements Token {

    private final String text;

    /**
     * @param text must be not null
     */
    public IdentifierToken(String text) {
        this.text = text;
    }

    @Override
    public String text() {
        return text;
    }

    @Override
    public TokenType type() {
        return IDENTIFIER;
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IdentifierToken)) {
            return false;
        }

        final IdentifierToken other = (IdentifierToken) obj;

        return text.equals(other.text);
    }

    @Override
    public String toString() {
        return "IDENTIFIER_TOKEN(" + text + ")";
    }
}
