package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.NAryNode;

import java.util.List;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class Values extends NAryNode<AST, Row> implements AST {

    public Values(List<Row> rows) {
        super(rows);
    }

    public static Values rows(final List<Row> rows) {
        return new Values(List.copyOf(rows));
    }

    public static Values rows(final Row... rows) {
        return new Values(List.of(rows));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(children())
                .toString();
    }

    public List<Row> rows() {
        return children();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Values other)) {
            return false;
        }

        return equalsNAry(other);
    }
}
