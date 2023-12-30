package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.tree.LeafNode;

import java.math.BigDecimal;
import java.util.function.Supplier;

public final class DecimalVariable extends LeafNode<Operation> implements DecimalOperation {

    private final Supplier<BigDecimal> supplier;

    public DecimalVariable(final Supplier<BigDecimal> supplier) {
        this.supplier = supplier;
    }

    @Override
    public BigDecimal evaluateBigDecimal() {
        return supplier.get();
    }

    @Override
    public boolean isConstant() {
        return false;
    }
}
