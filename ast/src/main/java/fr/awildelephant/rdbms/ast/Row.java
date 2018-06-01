package fr.awildelephant.rdbms.ast;

import java.util.List;

public final class Row implements AST {

    private final List<Integer> values;

    private Row(final List<Integer> values) {
        this.values = values;
    }

    public static Row row(final List<Integer> values) {
        return new Row(values);
    }

    @Override
    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Row)) {
            return false;
        }

        final Row other = (Row) obj;

        return values.equals(other.values);
    }
}
