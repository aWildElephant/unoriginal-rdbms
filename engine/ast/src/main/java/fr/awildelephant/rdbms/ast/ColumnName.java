package fr.awildelephant.rdbms.ast;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class ColumnName implements AST {

    private final String name;

    private ColumnName(String name) {
        this.name = name;
    }

    public static ColumnName columnName(String name) {
        return new ColumnName(name);
    }

    public String name() {
        return name;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(name)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ColumnName)) {
            return false;
        }

        final ColumnName other = (ColumnName) obj;

        return Objects.equals(name, other.name);
    }
}
