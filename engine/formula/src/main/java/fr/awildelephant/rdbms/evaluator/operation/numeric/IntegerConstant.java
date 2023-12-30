package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.evaluator.operation.ConstantOperation;

public final class IntegerConstant extends ConstantOperation implements IntegerOperation {

    private final Integer value;

    public IntegerConstant(final Integer value) {
        this.value = value;
    }

    @Override
    public Integer evaluateInteger() {
        return value;
    }
}
