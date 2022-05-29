package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.BinaryNode;

public final class CreateTable extends BinaryNode<AST, TableName, TableElementList> implements AST {

    public CreateTable(TableName tableName, TableElementList columns) {
        super(tableName, columns);
    }

    public static CreateTable createTable(TableName tableName, TableElementList columns) {
        return new CreateTable(tableName, columns);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public TableName tableName() {
        return firstChild();
    }

    public TableElementList columns() {
        return secondChild();
    }

    @Override
    public String toString() {
        return "CreateTable[tableName=" + firstChild() + ", columns=" + secondChild() + ']';
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CreateTable other)) {
            return false;
        }

        return equalsBinaryNode(other);
    }
}
