package fr.awildelephant.rdbms.ast;

import java.util.List;
import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class SortedSelect implements AST {

    private final List<? extends AST> outputColumns;
    private final SortSpecificationList sorting;
    private final AST inputTable;

    private SortedSelect(List<? extends AST> outputColumns, SortSpecificationList sorting, AST inputTable) {
        this.outputColumns = outputColumns;
        this.sorting = sorting;
        this.inputTable = inputTable;
    }

    public static SortedSelect select(List<? extends AST> output, AST inputTable) {
        return sortedSelect(output, null, inputTable);
    }

    public static SortedSelect sortedSelect(List<? extends AST> output, SortSpecificationList sorting, AST inputTable) {
        return new SortedSelect(output, sorting, inputTable);
    }

    public List<? extends AST> outputColumns() {
        return outputColumns;
    }

    public AST inputTable() {
        return inputTable;
    }

    public SortSpecificationList sorting() {
        return sorting;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("outputColumns", outputColumns)
                .append("sorting", sorting)
                .append("inputTable", inputTable)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(outputColumns, sorting, inputTable);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SortedSelect)) {
            return false;
        }

        final SortedSelect other = (SortedSelect) obj;

        return Objects.equals(outputColumns, other.outputColumns)
                && Objects.equals(sorting, other.sorting)
                && Objects.equals(inputTable, other.inputTable);
    }
}
