package fr.awildelephant.rdbms.ast;

import java.util.List;

public final class Row implements AST {

    private final List<Object> values;

    private Row(final List<Object> values) {
        this.values = values;
    }

    public static Row row(final List<Object> values) {
        return new Row(values);
    }

    public List<Object> values() {
        return values;
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
