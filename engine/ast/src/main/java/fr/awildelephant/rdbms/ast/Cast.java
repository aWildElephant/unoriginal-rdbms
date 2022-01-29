package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

public record Cast(AST input, ColumnType targetType) implements AST {

    public static Cast cast(final AST input, final ColumnType targetType) {
        return new Cast(input, targetType);
    }

    @Override
    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
