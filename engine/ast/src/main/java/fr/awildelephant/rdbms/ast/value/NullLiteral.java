package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.LeafNode;

public final class NullLiteral extends LeafNode<AST> implements AST {

    private static final NullLiteral NULL_VALUE = new NullLiteral();

    private NullLiteral() {

    }

    public static NullLiteral nullLiteral() {
        return NULL_VALUE;
    }

    @Override
    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
