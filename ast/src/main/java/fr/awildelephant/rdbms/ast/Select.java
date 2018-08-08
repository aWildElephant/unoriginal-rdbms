package fr.awildelephant.rdbms.ast;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Objects;

public final class Select implements AST {

    private final List<? extends AST> outputColumns;
    private final TableName inputTable;

    private Select(List<? extends AST> outputColumns, TableName inputTable) {
        this.outputColumns = outputColumns;
        this.inputTable = inputTable;
    }

    public static Select select(List<? extends AST> output, TableName inputTable) {
        return new Select(output, inputTable);
    }

    public List<? extends AST> outputColumns() {
        return outputColumns;
    }

    public TableName inputTable() {
        return inputTable;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("outputColumns", outputColumns)
                .append("inputTable", inputTable)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(outputColumns, inputTable);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Select)) {
            return false;
        }

        final Select other = (Select) obj;

        return Objects.equals(outputColumns, other.outputColumns)
                && Objects.equals(inputTable, other.inputTable);
    }
}
