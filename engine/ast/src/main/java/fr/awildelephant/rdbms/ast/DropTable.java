package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

public final class DropTable implements AST {

    private final TableName tableName;

    private DropTable(TableName tableName) {
        this.tableName = tableName;
    }

    public static DropTable dropTable(TableName tableName) {
        return new DropTable(tableName);
    }

    public TableName tableName() {
        return tableName;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tableName);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DropTable)) {
            return false;
        }

        final DropTable other = (DropTable) obj;

        return Objects.equals(tableName, other.tableName);
    }
}
