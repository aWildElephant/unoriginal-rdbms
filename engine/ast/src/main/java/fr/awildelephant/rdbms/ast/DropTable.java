package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

public final class DropTable extends UnaryNode<AST, TableName> implements AST {

    public DropTable(TableName tableName) {
        super(tableName);
    }

    public static DropTable dropTable(TableName tableName) {
        return new DropTable(tableName);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public TableName tableName() {
        return child();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DropTable other)) {
            return false;
        }

        return equalsUnaryNode(other);
    }

    @Override
    public String toString() {
        return "DropTable[tableName=" + child() + ']';
    }

}
