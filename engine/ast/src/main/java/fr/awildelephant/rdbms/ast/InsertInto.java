package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.BinaryNode;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class InsertInto extends BinaryNode<AST, TableName, AST> implements AST {

    private InsertInto(TableName targetTable, AST source) {
        super(targetTable, source);
    }

    public static InsertInto insertInto(TableName targetTable, AST source) {
        return new InsertInto(targetTable, source);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("target", targetTable())
                .append("source", source())
                .toString();
    }

    public TableName targetTable() {
        return firstChild();
    }

    public AST source() {
        return secondChild();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InsertInto other)) {
            return false;
        }

        return equalsBinaryNode(other);
    }
}
