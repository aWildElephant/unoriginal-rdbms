package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ASTVisitor;

public final class Placeholder implements AST {

    private static final Placeholder PLACEHOLDER = new Placeholder();

    private Placeholder() {

    }

    public static Placeholder placeholder() {
        return PLACEHOLDER;
    }

    @Override
    public String toString() {
        return "?";
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
