package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Collection;
import java.util.List;

public record InnerJoin(AST left, AST right, AST joinSpecification) implements AST {

    public static InnerJoin innerJoin(AST left, AST right, AST joinSpecification) {
        return new InnerJoin(left, right, joinSpecification);
    }

    @Override
    public Collection<? extends AST> children() {
        return List.of(left, right, joinSpecification);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
