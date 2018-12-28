package fr.awildelephant.rdbms.ast;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

public final class InsertInto implements AST {

    private final TableName targetTable;
    private final Rows rows;

    private InsertInto(TableName targetTable, Rows rows) {
        this.targetTable = targetTable;
        this.rows = rows;
    }

    public static InsertInto insertInto(TableName targetTable, Rows rows) {
        return new InsertInto(targetTable, rows);
    }

    public TableName targetTable() {
        return targetTable;
    }

    public Rows rows() {
        return rows;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetTable, rows);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InsertInto)) {
            return false;
        }

        final InsertInto other = (InsertInto) obj;

        return Objects.equals(targetTable, other.targetTable)
                && Objects.equals(rows, other.rows);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("target", targetTable)
                .append("content", rows)
                .toString();
    }
}
