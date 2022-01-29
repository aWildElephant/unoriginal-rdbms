package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.List;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public record Values(List<Row> rows) implements AST {

    public static Values rows(final List<Row> rows) {
        return new Values(List.copyOf(rows));
    }

    public static Values rows(final Row... rows) {
        return new Values(List.of(rows));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(rows)
                .toString();
    }
}
