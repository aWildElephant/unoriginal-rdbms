package fr.awildelephant.rdbms.evaluator.operation.bool;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.tree.UnaryNode;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.TRUE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.UNKNOWN;

// TODO: abstract class for 1-ary node
public final class NotOperation extends UnaryNode<Operation, BooleanOperation> implements BooleanOperation {

    private NotOperation(final BooleanOperation input) {
        super(input);
    }

    public static NotOperation notOperation(final BooleanOperation input) {
        return new NotOperation(input);
    }

    @Override
    public ThreeValuedLogic evaluateBoolean() {
        final ThreeValuedLogic childValue = child().evaluateBoolean();
        return switch (childValue) {
            case UNKNOWN -> UNKNOWN;
            case FALSE -> TRUE;
            case TRUE -> FALSE;
        };
    }

    @Override
    public boolean isConstant() {
        return child().isConstant();
    }
}
