package fr.awildelephant.rdbms.evaluator.operation.cast;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.numeric.IntegerOperation;
import fr.awildelephant.rdbms.evaluator.operation.text.TextOperation;
import fr.awildelephant.rdbms.tree.UnaryNode;

public final class IntegerToTextCastOperation extends UnaryNode<Operation, IntegerOperation> implements TextOperation {

    public IntegerToTextCastOperation(final IntegerOperation input) {
        super(input);
    }

    @Override
    public String evaluateString() {
        final Integer value = child().evaluateInteger();
        if (value == null) {
            return null;
        }
        return Integer.toString(value);
    }

    @Override
    public boolean isConstant() {
        return child().isConstant();
    }
}
