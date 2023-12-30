package fr.awildelephant.rdbms.evaluator.operation.text;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.tree.LeafNode;

public final class TextConstant extends LeafNode<Operation> implements TextOperation {

    private final String value;

    public TextConstant(final String value) {
        this.value = value;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public String evaluateString() {
        return value;
    }
}
