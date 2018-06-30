package fr.awildelephant.rdbms.ast;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Objects;

public final class Select implements AST {

    private final boolean distinct;
    private final List<? extends AST> outputColumns;
    private final TableName inputTable;

    private Select(boolean distinct, List<? extends AST> outputColumns, TableName inputTable) {
        this.distinct = distinct;
        this.outputColumns = outputColumns;
        this.inputTable = inputTable;
    }

    public static Select select(List<? extends AST> output, TableName inputTable) {
        return select(false, output, inputTable);
    }

    public static Select select(boolean distinct, List<? extends AST> output, TableName inputTable) {
        return new Select(distinct, output, inputTable);
    }

    public List<? extends AST> outputColumns() {
        return outputColumns;
    }

    public TableName inputTable() {
        return inputTable;
    }

    public boolean distinct() {
        return distinct;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("distinct", distinct)
                .append("outputColumns", outputColumns)
                .append("inputTable", inputTable)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinct, outputColumns, inputTable);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Select)) {
            return false;
        }

        final Select other = (Select) obj;

        return distinct == other.distinct
                && Objects.equals(outputColumns, other.outputColumns)
                && Objects.equals(inputTable, other.inputTable);
    }
}
