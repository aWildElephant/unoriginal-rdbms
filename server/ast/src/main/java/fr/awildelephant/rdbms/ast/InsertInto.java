package fr.awildelephant.rdbms.ast;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

public final class InsertInto implements AST {

    private final TableName targetTable;
    private final Values values;

    private InsertInto(TableName targetTable, Values values) {
        this.targetTable = targetTable;
        this.values = values;
    }

    public static InsertInto insertInto(TableName targetTable, Values values) {
        return new InsertInto(targetTable, values);
    }

    public TableName targetTable() {
        return targetTable;
    }

    public Values rows() {
        return values;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetTable, values);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InsertInto)) {
            return false;
        }

        final InsertInto other = (InsertInto) obj;

        return Objects.equals(targetTable, other.targetTable)
                && Objects.equals(values, other.values);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("target", targetTable)
                .append("content", values)
                .toString();
    }
}
