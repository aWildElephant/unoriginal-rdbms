package fr.awildelephant.rdbms.ast;

import java.util.Objects;

public final class ColumnDefinition implements AST {

    // TODO: use java.sql.Types?
    public static final int DATE = 0;
    public static final int INTEGER = 1;
    public static final int TEXT = 2;
    public static final int DECIMAL = 3;

    private final String columnName;
    private final int columnType;

    private ColumnDefinition(String name, int type) {
        this.columnName = name;
        this.columnType = type;
    }

    public static ColumnDefinition column(String name, int type) {
        return new ColumnDefinition(name, type);
    }

    public String columnName() {
        return columnName;
    }

    public int columnType() {
        return columnType;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, columnType);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ColumnDefinition)) {
            return false;
        }

        final ColumnDefinition other = (ColumnDefinition) obj;

        return columnType == other.columnType
                && Objects.equals(columnName, other.columnName);
    }
}
