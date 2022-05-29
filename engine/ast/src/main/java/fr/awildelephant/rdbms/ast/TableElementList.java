package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.constraints.ForeignKeyConstraint;
import fr.awildelephant.rdbms.ast.constraints.NotNullConstraint;
import fr.awildelephant.rdbms.ast.constraints.UniqueConstraint;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.NAryNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static fr.awildelephant.rdbms.ast.ColumnDefinition.column;

public final class TableElementList extends NAryNode<AST, ColumnDefinition> implements AST {

    private final List<NotNullConstraint> notNullConstraints;
    private final List<UniqueConstraint> uniqueConstraints;
    private final List<ForeignKeyConstraint> foreignKeyConstraints;

    public TableElementList(List<ColumnDefinition> columns, List<NotNullConstraint> notNullConstraints, List<UniqueConstraint> uniqueConstraints, List<ForeignKeyConstraint> foreignKeyConstraints) {
        super(columns);
        this.notNullConstraints = notNullConstraints;
        this.uniqueConstraints = uniqueConstraints;
        this.foreignKeyConstraints = foreignKeyConstraints;
    }

    public static Builder tableElementList() {
        return new Builder();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public List<ColumnDefinition> columns() {
        return children();
    }

    public List<NotNullConstraint> notNullConstraints() {
        return notNullConstraints;
    }

    public List<UniqueConstraint> uniqueConstraints() {
        return uniqueConstraints;
    }

    public List<ForeignKeyConstraint> foreignKeyConstraints() {
        return foreignKeyConstraints;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TableElementList other)) {
            return false;
        }

        return Objects.equals(this.notNullConstraints, other.notNullConstraints)
                && Objects.equals(this.uniqueConstraints, other.uniqueConstraints)
                && Objects.equals(this.foreignKeyConstraints, other.foreignKeyConstraints)
                && equalsNAry(other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(children(), notNullConstraints, uniqueConstraints, foreignKeyConstraints);
    }

    @Override
    public String toString() {
        return "TableElementList[" +
                "columns=" + children() + ", " +
                "notNullConstraints=" + notNullConstraints + ", " +
                "uniqueConstraints=" + uniqueConstraints + ", " +
                "foreignKeyConstraints=" + foreignKeyConstraints + ']';
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
