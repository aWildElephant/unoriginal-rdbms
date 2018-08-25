package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ASTVisitor;

public final class IntegerLiteral implements AST {

    private final int value;

    private IntegerLiteral(int value) {
        this.value = value;
    }

    public static IntegerLiteral integerLiteral(int value) {
        return new IntegerLiteral(value);
    }

    public int value() {
        return value;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntegerLiteral)) {
            return false;
        }

        final IntegerLiteral other = (IntegerLiteral) obj;

        return value == other.value;
    }
}
