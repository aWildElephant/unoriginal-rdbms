package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.NAryNode;

import java.util.List;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class Row extends NAryNode<AST, AST> implements AST {

    public Row(List<AST> values) {
        super(values);
    }

    public static Row row(final List<AST> values) {
        return new Row(List.copyOf(values));
    }

    public static Row row(final AST... values) {
        return new Row(List.of(values));
    }

    public List<AST> values() {
        return children();
    }

    @Override
    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(children())
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Row other)) {
            return false;
        }

        return equalsNAry(other);
    }
}
