package fr.awildelephant.rdbms.ast;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Objects;

public final class Rows implements AST {

    private final List<Row> rows;

    private Rows(List<Row> rows) {
        this.rows = rows;
    }

    public static Rows rows(final List<Row> rows) {
        return new Rows(List.copyOf(rows));
    }

    public static Rows rows (final Row... rows) {
        return new Rows(List.of(rows));
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
        return Objects.hashCode(rows);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Rows)) {
            return false;
        }

        final Rows other = (Rows) obj;

        return Objects.equals(rows, other.rows);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(rows)
                .toString();
    }
}
