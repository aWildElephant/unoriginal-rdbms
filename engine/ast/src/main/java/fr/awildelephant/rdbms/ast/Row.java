package fr.awildelephant.rdbms.ast;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class Row implements AST {

    private final List<AST> values;

    private Row(final List<AST> values) {
        this.values = values;
    }

    public static Row row(final List<AST> values) {
        return new Row(List.copyOf(values));
    }

    public static Row row(final AST... values) {
        return new Row(List.of(values));
    }

    public List<AST> values() {
        return values;
    }

    @Override
    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(values);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Row)) {
            return false;
        }

        final Row other = (Row) obj;

        return Objects.equals(values, other.values);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(values)
                .toString();
    }
}
