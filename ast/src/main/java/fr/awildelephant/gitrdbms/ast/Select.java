package fr.awildelephant.gitrdbms.ast;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

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
        return outputColumns.hashCode() * 32 + inputTable.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Select)) {
            return false;
        }

        final Select other = (Select) obj;

        return outputColumns.equals(other.outputColumns)
                && inputTable.equals(other.inputTable);
    }
}
