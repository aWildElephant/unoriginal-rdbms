package fr.awildelephant.rdbms.ast;

public final class CreateTable implements AST {

    private final TableName tableName;
    private final AST columns;

    /**
     * @param tableName must be not null
     * @param columns   must be not null
     */
    private CreateTable(TableName tableName, AST columns) {
        this.tableName = tableName;
        this.columns = columns;
    }

    public static CreateTable createTable(TableName tableName, AST columns) {
        return new CreateTable(tableName, columns);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return tableName.hashCode() * 32 + columns.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CreateTable)) {
            return false;
        }

        final CreateTable other = (CreateTable) obj;

        return tableName.equals(other.tableName) && columns.equals(other.columns);
    }
}
