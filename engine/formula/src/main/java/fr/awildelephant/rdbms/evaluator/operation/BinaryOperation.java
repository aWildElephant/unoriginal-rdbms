package fr.awildelephant.rdbms.evaluator.operation;

public abstract class BinaryOperation implements Operation {

    protected final Operation left;
    protected final Operation right;

    protected BinaryOperation(Operation left, Operation right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isConstant() {
        return left.isConstant() && right.isConstant();
    }
}
