package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.constraints.ForeignKeyConstraint;
import fr.awildelephant.rdbms.ast.constraints.NotNullConstraint;
import fr.awildelephant.rdbms.ast.constraints.UniqueConstraint;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static fr.awildelephant.rdbms.ast.ColumnDefinition.column;

public final class TableElementList implements AST {

    private final List<ColumnDefinition> columns;
    private final List<NotNullConstraint> notNullConstraints;
    private final List<UniqueConstraint> uniqueConstraints;
    private final List<ForeignKeyConstraint> foreignKeyConstraints;

    private TableElementList(TableElementList.Builder builder) {
        this.columns = builder.columns();
        this.notNullConstraints = builder.notNullConstraints();
        this.uniqueConstraints = builder.uniqueConstraints();
        this.foreignKeyConstraints = builder.foreignKeyConstraints();
    }

    public static TableElementList.Builder tableElementList() {
        return new TableElementList.Builder();
    }

    public List<ColumnDefinition> columns() {
        return columns;
    }

    public Iterable<NotNullConstraint> notNullConstraints() {
        return notNullConstraints;
    }

    public Iterable<UniqueConstraint> uniqueConstraints() {
        return uniqueConstraints;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columns, notNullConstraints, uniqueConstraints, foreignKeyConstraints);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final TableElementList other)) {
            return false;
        }

        return Objects.equals(columns, other.columns)
                && Objects.equals(notNullConstraints, other.notNullConstraints)
                && Objects.equals(uniqueConstraints, other.uniqueConstraints)
                && Objects.equals(foreignKeyConstraints, other.foreignKeyConstraints);
    }

    public static class Builder {

        private final List<ColumnDefinition> columns = new LinkedList<>();
        private final List<NotNullConstraint> notNullConstraints = new LinkedList<>();
        private final List<UniqueConstraint> uniqueConstraints = new LinkedList<>();
        private final List<ForeignKeyConstraint> foreignKeyConstraints = new LinkedList<>();

        public Builder addColumn(String columnName, int columnType) {
            columns.add(column(columnName, columnType));

            return this;
        }

        public Builder addNotNullConstraint(String columnName) {
            notNullConstraints.add(new NotNullConstraint(columnName));

            return this;
        }

        public Builder addUniqueConstraint(String columnName) {
            return addUniqueConstraint(Set.of(columnName));
        }

        public Builder addUniqueConstraint(Set<String> columnNames) {
            uniqueConstraints.add(new UniqueConstraint(columnNames));

            return this;
        }

        public Builder addForeignKeyConstraint(Set<String> columns, String targetTable, Set<String> targetColumns) {
            foreignKeyConstraints.add(new ForeignKeyConstraint(targetTable, columns, targetColumns));

            return this;
        }

        List<ColumnDefinition> columns() {
            return columns;
        }

        List<NotNullConstraint> notNullConstraints() {
            return notNullConstraints;
        }

        List<UniqueConstraint> uniqueConstraints() {
            return uniqueConstraints;
        }

        List<ForeignKeyConstraint> foreignKeyConstraints() {
            return foreignKeyConstraints;
        }

        public TableElementList build() {
            return new TableElementList(this);
        }
    }
}
