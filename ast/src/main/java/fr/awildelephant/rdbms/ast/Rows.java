package fr.awildelephant.rdbms.ast;

import java.util.List;

public final class Rows implements AST {

    private final List<Row> rows;

    private Rows(List<Row> rows) {
        this.rows = rows;
    }

    public static Rows rows(final List<Row> rows) {
        return new Rows(rows);
    }

    public List<Row> rows() {
        return rows;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return rows.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Rows)) {
            return false;
        }

        final Rows other = (Rows) obj;

        return rows.equals(other.rows);
    }
}
