package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

public record LeftJoin(AST left, AST right, AST joinSpecification) implements AST {

    public static LeftJoin leftJoin(AST left, AST right, AST joinSpecification) {
        return new LeftJoin(left, right, joinSpecification);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
