package fr.awildelephant.gitrdbms.parser.ast;

public final class ColumnDefinition implements AST {

    public static final int INTEGER = 0;

    private final String columnName;
    private final int columnType;

    /**
     * @param columnName must be not null
     * @param columnType must be not null
     */
    public ColumnDefinition(String columnName, int columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
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
