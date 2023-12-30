package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.tree.LeafNode;

public final class LongConstant extends LeafNode<Operation> implements LongOperation {

    private final Long value;

    public LongConstant(final Long value) {
        this.value = value;
    }

    @Override
    public Long evaluateLong() {
        return value;
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
