package fr.awildelephant.gitrdbms.parser.ast;

import java.util.List;

public final class CreateTable implements AST {

    private final String tableName;
    private final List<ColumnDefinition> columns;

    /**
     * @param tableName must be not null
     * @param columns must be not null
     */
    public CreateTable(String tableName, List<ColumnDefinition> columns) {
        this.tableName = tableName;
        this.columns = columns;
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
