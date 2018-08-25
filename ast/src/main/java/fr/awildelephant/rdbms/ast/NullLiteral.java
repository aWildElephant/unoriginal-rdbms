package fr.awildelephant.rdbms.ast;

public final class NullLiteral implements AST {

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
