package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

public class Truncate extends UnaryNode<AST, TableName> implements AST {

    private Truncate(TableName tableName) {
        super(tableName);
    }

    public static Truncate truncate(TableName tableName) {
        return new Truncate(tableName);
    }

    public TableName tableName() {
        return child();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
