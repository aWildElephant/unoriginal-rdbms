package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

public record DropTable(TableName tableName) implements AST {

    public static DropTable dropTable(TableName tableName) {
        return new DropTable(tableName);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
