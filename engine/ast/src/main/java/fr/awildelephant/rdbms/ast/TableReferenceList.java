package fr.awildelephant.rdbms.ast;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Objects;

public final class TableReferenceList implements AST {

    private final List<AST> tableReferences;

    private TableReferenceList(List<AST> tableReferences) {
        this.tableReferences = tableReferences;
    }

    public static TableReferenceList tableReferenceList(List<AST> tableReferences) {
        return new TableReferenceList(tableReferences);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("tableReferences", tableReferences).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tableReferences);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TableReferenceList)) {
            return false;
        }

        final TableReferenceList other = (TableReferenceList) obj;

        return Objects.equals(tableReferences, other.tableReferences);
    }
}
