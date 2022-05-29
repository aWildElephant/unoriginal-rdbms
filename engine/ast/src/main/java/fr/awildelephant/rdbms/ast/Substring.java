package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Collection;
import java.util.List;

public record Substring(AST input, AST start, AST length) implements AST {

    public static Substring substring(AST input, AST start, AST length) {
        return new Substring(input, start, length);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Collection<? extends AST> children() {
        return List.of(input, start, length);
    }
}
