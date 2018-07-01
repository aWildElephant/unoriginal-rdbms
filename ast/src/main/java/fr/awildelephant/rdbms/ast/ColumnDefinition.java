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
    private final boolean unique;

    private ColumnDefinition(Builder builder) {
        this.columnName = builder.columnName();
        this.columnType = builder.columnType();
        this.notNull = builder.isNotNull();
        this.unique = builder.isUnique();
    }

    public static Builder builder(String columnName, int columnType) {
        return new Builder(columnName, columnType);
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

    public boolean unique() {
        return unique;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, columnType, notNull, unique);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ColumnDefinition)) {
            return false;
        }

        final ColumnDefinition other = (ColumnDefinition) obj;

        return columnType == other.columnType
                && notNull == other.notNull
                && unique == other.unique
                && Objects.equals(columnName, other.columnName);
    }

    public static class Builder {

        private final String columnName;
        private final int columnType;

        private boolean notNull;
        private boolean unique;

        Builder(String columnName, int columnType) {
            this.columnName = columnName;
            this.columnType = columnType;
        }

        public Builder notNull() {
            notNull = true;

            return this;
        }

        public Builder unique() {
            unique = true;

            return this;
        }

        String columnName() {
            return columnName;
        }

        int columnType() {
            return columnType;
        }

        boolean isNotNull() {
            return notNull;
        }

        boolean isUnique() {
            return unique;
        }

        public ColumnDefinition build() {
            return new ColumnDefinition(this);
        }
    }
}
