package fr.awildelephant.rdbms.ast;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class Values implements AST {

    private final List<Row> rows;

    private Values(List<Row> rows) {
        this.rows = rows;
    }

    public static Values rows(final List<Row> rows) {
        return new Values(List.copyOf(rows));
    }

    public static Values rows(final Row... rows) {
        return new Values(List.of(rows));
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
        if (!(obj instanceof Values)) {
            return false;
        }

        final Values other = (Values) obj;

        return Objects.equals(rows, other.rows);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(rows)
                .toString();
    }
}
