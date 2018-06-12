package fr.awildelephant.rdbms.ast;

public final class ColumnDefinition implements AST {

    public static final int INTEGER = 0;

    private final String columnName;
    private final int columnType;

    /**
     * @param columnName must be not null
     * @param columnType must be not null
     */
    private ColumnDefinition(String columnName, int columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
    }

    public static ColumnDefinition columnDefinition(String columnName, int columnType) {
        return new ColumnDefinition(columnName, columnType);
    }

    public String columnName() {
        return columnName;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return columnName.hashCode() * 32 + columnType;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ColumnDefinition)) {
            return false;
        }

        final ColumnDefinition other = (ColumnDefinition) obj;

        return columnType == other.columnType
                && columnName.equals(other.columnName);
    }
}
