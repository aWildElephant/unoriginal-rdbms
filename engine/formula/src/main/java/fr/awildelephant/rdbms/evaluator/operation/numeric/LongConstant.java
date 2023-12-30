package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.evaluator.operation.ConstantOperation;

public final class LongConstant extends ConstantOperation implements LongOperation {

    private final Long value;

    public LongConstant(final Long value) {
        this.value = value;
    }

    @Override
    public Long evaluateLong() {
        return value;
    }
}
