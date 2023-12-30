package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.tree.LeafNode;

public final class IntegerConstant extends LeafNode<Operation> implements IntegerOperation {

    private final Integer value;

    public IntegerConstant(final Integer value) {
        this.value = value;
    }

    @Override
    public Integer evaluate() {
        return value;
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
