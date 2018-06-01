package fr.awildelephant.rdbms.ast;

public final class InsertInto implements AST {

    private final TableName targetTable;
    private final Rows rows;

    private InsertInto(TableName targetTable, Rows rows) {
        this.targetTable = targetTable;
        this.rows = rows;
    }

    public static InsertInto insertInto(TableName targetTable, Rows value) {
        return new InsertInto(targetTable, value);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return targetTable.hashCode() * 32 + rows.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InsertInto)) {
            return false;
        }

        final InsertInto other = (InsertInto) obj;

        return rows.equals(other.rows)
                && targetTable.equals(other.targetTable);
    }
}
