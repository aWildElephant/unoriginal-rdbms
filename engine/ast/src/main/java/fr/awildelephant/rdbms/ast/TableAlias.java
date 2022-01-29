package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public record TableAlias(AST input, String alias) implements AST {

    public static TableAlias tableAlias(AST input, String alias) {
        return new TableAlias(input, alias);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("input", input)
                .append("alias", alias)
                .toString();
    }
}
