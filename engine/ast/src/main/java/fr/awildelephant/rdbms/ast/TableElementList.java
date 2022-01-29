package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.constraints.ForeignKeyConstraint;
import fr.awildelephant.rdbms.ast.constraints.NotNullConstraint;
import fr.awildelephant.rdbms.ast.constraints.UniqueConstraint;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static fr.awildelephant.rdbms.ast.ColumnDefinition.column;

public record TableElementList(List<ColumnDefinition> columns, List<NotNullConstraint> notNullConstraints, List<UniqueConstraint> uniqueConstraints, List<ForeignKeyConstraint> foreignKeyConstraints) implements AST {

    public static TableElementList.Builder tableElementList() {
        return new TableElementList.Builder();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public static final class Builder {

        private final List<ColumnDefinition> columns = new LinkedList<>();
        private final List<NotNullConstraint> notNullConstraints = new LinkedList<>();
        private final List<UniqueConstraint> uniqueConstraints = new LinkedList<>();
        private final List<ForeignKeyConstraint> foreignKeyConstraints = new LinkedList<>();

        public Builder addColumn(String columnName, ColumnType columnType) {
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
            return new TableElementList(this.columns(), this.notNullConstraints(), this.uniqueConstraints(), this.foreignKeyConstraints());
        }
    }
}
