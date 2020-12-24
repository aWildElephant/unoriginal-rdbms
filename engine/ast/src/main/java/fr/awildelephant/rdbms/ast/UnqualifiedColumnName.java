package fr.awildelephant.rdbms.ast;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class UnqualifiedColumnName implements ColumnName {

    private final String name;

    private UnqualifiedColumnName(String name) {
        this.name = name;
    }

    public static UnqualifiedColumnName unqualifiedColumnName(String name) {
        return new UnqualifiedColumnName(name);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UnqualifiedColumnName)) {
            return false;
        }

        final UnqualifiedColumnName other = (UnqualifiedColumnName) obj;

        return Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("name", name)
                .toString();
    }
}
