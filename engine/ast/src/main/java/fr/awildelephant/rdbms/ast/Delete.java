package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.BinaryNode;

public final class Delete extends BinaryNode<AST, TableName, AST> implements AST {

    private Delete(final TableName tableName, final AST predicate) {
        super(tableName, predicate);
    }

    public static Delete delete(final TableName tableName, final AST predicate) {
        return new Delete(tableName, predicate);
    }

    public String tableName() {
        return leftChild().name();
    }

    public AST predicate() {
        return rightChild();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
