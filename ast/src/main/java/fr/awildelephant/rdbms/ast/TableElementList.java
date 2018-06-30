package fr.awildelephant.rdbms.ast;

import java.util.List;
import java.util.Objects;

public final class TableElementList implements AST {

    private final List<ColumnDefinition> elements;

    private TableElementList(List<ColumnDefinition> elements) {
        this.elements = elements;
    }

    public static TableElementList tableElementList(List<ColumnDefinition> elements) {
        return new TableElementList(elements);
    }

    public List<ColumnDefinition> elements() {
        return elements;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(elements);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TableElementList)) {
            return false;
        }

        final TableElementList other = (TableElementList) obj;

        return Objects.equals(elements, other.elements);
    }
}
