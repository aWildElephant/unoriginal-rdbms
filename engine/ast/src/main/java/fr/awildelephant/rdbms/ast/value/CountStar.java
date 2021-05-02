package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

public final class CountStar implements AST {

    private static final CountStar INSTANCE = new CountStar();

    private CountStar() {

    }

    public static CountStar countStar() {
        return INSTANCE;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
