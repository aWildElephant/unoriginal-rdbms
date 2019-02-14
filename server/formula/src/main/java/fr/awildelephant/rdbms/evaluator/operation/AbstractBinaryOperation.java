package fr.awildelephant.rdbms.evaluator.operation;

public abstract class AbstractBinaryOperation implements Operation {

    protected final Operation left;
    protected final Operation right;

    protected AbstractBinaryOperation(Operation left, Operation right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isConstant() {
        return left.isConstant() && right.isConstant();
    }
}
