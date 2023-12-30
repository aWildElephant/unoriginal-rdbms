package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.tree.LeafNode;

import java.math.BigDecimal;

public final class DecimalConstant extends LeafNode<Operation> implements DecimalOperation {

    private final BigDecimal value;

    public DecimalConstant(BigDecimal value) {
        this.value = value;
    }

    @Override
    public BigDecimal evaluateBigDecimal() {
        return value;
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
