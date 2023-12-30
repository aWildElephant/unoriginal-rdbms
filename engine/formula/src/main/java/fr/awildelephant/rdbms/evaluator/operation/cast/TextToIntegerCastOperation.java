package fr.awildelephant.rdbms.evaluator.operation.cast;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.numeric.IntegerOperation;
import fr.awildelephant.rdbms.evaluator.operation.text.TextOperation;
import fr.awildelephant.rdbms.tree.UnaryNode;

public final class TextToIntegerCastOperation extends UnaryNode<Operation, TextOperation> implements IntegerOperation {

    public TextToIntegerCastOperation(final TextOperation input) {
        super(input);
    }

    @Override
    public boolean isConstant() {
        return child().isConstant();
    }

    @Override
    public Integer evaluateInteger() {
        final String value = child().evaluateString();
        if (value == null) {
            return null;
        }
        return Integer.parseInt(value);
    }
}
