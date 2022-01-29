package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.annotation.Intermediate;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

@Intermediate
public record SemiJoin(AST left, AST right, AST predicate) implements AST {

    public static SemiJoin semiJoin(AST left, AST right, AST predicate) {
        return new SemiJoin(left, right, predicate);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
