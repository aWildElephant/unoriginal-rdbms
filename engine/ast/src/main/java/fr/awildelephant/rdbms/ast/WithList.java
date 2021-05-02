package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.List;
import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class WithList implements AST {

    private final List<WithElement> elements;

    private WithList(List<WithElement> elements) {
        this.elements = elements;
    }

    public static WithList withList(List<WithElement> elements) {
        return new WithList(elements);
    }

    public List<WithElement> elements() {
        return elements;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(elements);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WithList)) {
            return false;
        }

        final WithList other = (WithList) obj;

        return Objects.equals(elements, other.elements);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("elements", elements)
                .toString();
    }
}
