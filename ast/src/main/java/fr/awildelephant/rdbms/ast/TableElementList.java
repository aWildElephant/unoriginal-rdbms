package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.constraints.NotNullConstraint;
import fr.awildelephant.rdbms.ast.constraints.UniqueConstraint;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static fr.awildelephant.rdbms.ast.ColumnDefinition.column;

public final class TableElementList implements AST {

    private final List<ColumnDefinition> columns;
    private final List<NotNullConstraint> notNullConstraints;
    private final List<UniqueConstraint> uniqueConstraints;

    private TableElementList(TableElementList.Builder builder) {
        this.columns = builder.columns();
        this.notNullConstraints = builder.notNullConstraints();
        this.uniqueConstraints = builder.uniqueConstraints();
    }

    public static TableElementList.Builder tableElementList() {
        return new TableElementList.Builder();
    }

    public List<ColumnDefinition> columns() {
        return columns;
    }

    public Iterable<UniqueConstraint> uniqueConstraints() {
        return uniqueConstraints;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Iterable<NotNullConstraint> notNullConstraints() {
        return notNullConstraints;
    }

    @Override
    public int hashCode() {
        return Objects.hash(columns, notNullConstraints, uniqueConstraints);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TableElementList)) {
            return false;
        }

        final TableElementList other = (TableElementList) obj;

        return Objects.equals(columns, other.columns)
                && Objects.equals(notNullConstraints, other.notNullConstraints)
                && Objects.equals(uniqueConstraints, other.uniqueConstraints);
    }

    public static class Builder {

        private List<ColumnDefinition> columns = new LinkedList<>();
        private List<NotNullConstraint> notNullConstraints = new LinkedList<>();
        private List<UniqueConstraint> uniqueConstraints = new LinkedList<>();

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

        List<ColumnDefinition> columns() {
            return columns;
        }

        List<NotNullConstraint> notNullConstraints() {
            return notNullConstraints;
        }

        List<UniqueConstraint> uniqueConstraints() {
            return uniqueConstraints;
        }

        public TableElementList build() {
            return new TableElementList(this);
        }
    }
}
