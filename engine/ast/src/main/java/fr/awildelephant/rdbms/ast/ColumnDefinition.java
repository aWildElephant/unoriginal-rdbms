package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.LeafNode;

import java.util.Objects;

public final class ColumnDefinition extends LeafNode<AST> implements AST {

    private final String columnName;
    private final ColumnType columnType;

    public ColumnDefinition(String columnName, ColumnType columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
    }

    public static ColumnDefinition column(String name, ColumnType type) {
        return new ColumnDefinition(name, type);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String columnName() {
        return columnName;
    }

    public ColumnType columnType() {
        return columnType;
    }

    @Override
    public String toString() {
        return "ColumnDefinition[" +
                "columnName=" + columnName + ", " +
                "columnType=" + columnType + ']';
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, columnType);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ColumnDefinition other)) {
            return false;
        }

        return Objects.equals(columnName, other.columnName)
                && Objects.equals(columnType, other.columnType);
    }
}
