package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.LeafNode;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import java.util.Objects;

import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.TRUE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.UNKNOWN;

public final class BooleanLiteral extends LeafNode<AST> implements AST {

    private final ThreeValuedLogic value;

    private BooleanLiteral(ThreeValuedLogic value) {
        this.value = value;
    }

    public ThreeValuedLogic value() {
        return value;
    }

    public static BooleanLiteral trueLiteral() {
        return new BooleanLiteral(TRUE);
    }

    public static BooleanLiteral falseLiteral() {
        return new BooleanLiteral(FALSE);
    }

    public static BooleanLiteral unknownLiteral() {
        return new BooleanLiteral(UNKNOWN);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BooleanLiteral other)) {
            return false;
        }

        return Objects.equals(value, other.value);
    }
}
