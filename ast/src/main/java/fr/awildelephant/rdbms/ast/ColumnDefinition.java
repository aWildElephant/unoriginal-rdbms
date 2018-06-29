package fr.awildelephant.rdbms.ast;

import java.util.Objects;

public final class ColumnDefinition implements AST {

    // TODO: use java.sql.Types?
    public static final int INTEGER = 0;
    public static final int TEXT = 1;
    public static final int DECIMAL = 2;

    private final String columnName;
    private final int columnType;
    private final boolean notNull;

    private ColumnDefinition(String columnName, int columnType, boolean notNull) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.notNull = notNull;
    }

    public static ColumnDefinition columnDefinition(String columnName, int columnType) {
        return columnDefinition(columnName, columnType, false);
    }

    public static ColumnDefinition columnDefinition(String columnName, int columnType, boolean notNull) {
        return new ColumnDefinition(columnName, columnType, notNull);
    }

    public String columnName() {
        return columnName;
    }

    public int columnType() {
        return columnType;
    }

    public boolean notNull() {
        return notNull;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, columnType, notNull);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ColumnDefinition)) {
            return false;
        }

        final ColumnDefinition other = (ColumnDefinition) obj;

        return columnType == other.columnType
                && notNull == other.notNull
                && Objects.equals(columnName, other.columnName);
    }
}
