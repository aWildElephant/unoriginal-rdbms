package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.LeafNode;

public final class Asterisk extends LeafNode<AST> implements AST {

    private static final Asterisk ASTERISK = new Asterisk();

    private Asterisk() {

    }

    public static Asterisk asterisk() {
        return ASTERISK;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
