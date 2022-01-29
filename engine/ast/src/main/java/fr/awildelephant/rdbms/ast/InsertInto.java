package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public record InsertInto(TableName targetTable, Values rows) implements AST {

    public static InsertInto insertInto(TableName targetTable, Values rows) {
        return new InsertInto(targetTable, rows);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("target", targetTable)
                .append("content", rows)
                .toString();
    }
}
