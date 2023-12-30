package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.evaluator.operation.ConstantOperation;

import java.math.BigDecimal;

public final class DecimalConstant extends ConstantOperation implements DecimalOperation {

    private final BigDecimal value;

    public DecimalConstant(BigDecimal value) {
        this.value = value;
    }

    @Override
    public BigDecimal evaluateBigDecimal() {
        return value;
    }
}
