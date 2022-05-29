package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.BinaryNode;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class InsertInto extends BinaryNode<AST, TableName, Values> implements AST {

    public InsertInto(TableName targetTable, Values rows) {
        super(targetTable, rows);
    }

    public static InsertInto insertInto(TableName targetTable, Values rows) {
        return new InsertInto(targetTable, rows);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("target", targetTable())
                .append("content", rows())
                .toString();
    }

    public TableName targetTable() {
        return firstChild();
    }

    public Values rows() {
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
