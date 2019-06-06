package fr.awildelephant.rdbms.ast;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Objects;

public final class TableReferenceList implements AST {

    private final AST first;
    private final AST second;
    private final List<AST> others;

    private TableReferenceList(AST first, AST second, List<AST> others) {
        this.first = first;
        this.second = second;
        this.others = others;
    }

    public static TableReferenceList tableReferenceList(AST first, AST second, List<AST> others) {
        return new TableReferenceList(first, second, others);
    }

    public AST first() {
        return first;
    }

    public AST second() {
        return second;
    }

    public List<AST> others() {
        return others;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("first", first)
                .append("second", second)
                .append("others", others)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, others);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TableReferenceList)) {
            return false;
        }

        final TableReferenceList other = (TableReferenceList) obj;

        return Objects.equals(first, other.first)
                && Objects.equals(second, other.second)
                && Objects.equals(others, other.others);
    }
}
