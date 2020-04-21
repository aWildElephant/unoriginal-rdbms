package fr.awildelephant.rdbms.lexer;

public final class LexerSnapshot {

    private final int cursor;

    public LexerSnapshot(int cursor) {
        this.cursor = cursor;
    }

    public int cursor() {
        return cursor;
    }
}
