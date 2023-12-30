package fr.awildelephant.rdbms.evaluator.operation.interval;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.tree.LeafNode;

import java.time.Period;

public final class IntervalConstant extends LeafNode<Operation> implements IntervalOperation {

    private final Period value;

    public IntervalConstant(final Period value) {
        this.value = value;
    }

    @Override
    public Period evaluatePeriod() {
        return value;
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
