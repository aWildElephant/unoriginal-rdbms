package fr.awildelephant.rdbms.ast;

import java.util.Objects;

public final class Value implements AST {

    private final Object obj;

    private Value(final Object obj) {
        this.obj = obj;
    }

    public static Value value(final Object value) {
        return new Value(value);
    }

    public Object object() {
        return obj;
    }

    @Override
    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(obj);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Value)) {
            return false;
        }

        final Value other = (Value) obj;

        return Objects.equals(this.obj, other.obj);
    }
}
