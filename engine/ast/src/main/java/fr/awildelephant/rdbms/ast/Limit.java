package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public record Limit(AST input, int limit) implements AST {

    public static Limit limit(AST input, int limit) {
        return new Limit(input, limit);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("input", input)
                .append("limit", limit)
                .toString();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
