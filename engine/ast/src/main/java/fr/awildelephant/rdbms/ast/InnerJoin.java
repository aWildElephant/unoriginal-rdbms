package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

public record InnerJoin(AST left, AST right, AST joinSpecification) implements AST {

    public static InnerJoin innerJoin(AST left, AST right, AST joinSpecification) {
        return new InnerJoin(left, right, joinSpecification);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
