package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.tree.BinaryNode;

public abstract class BinaryOperation<L extends Operation, R extends Operation> extends BinaryNode<Operation, L, R> implements Operation {

    protected BinaryOperation(L left, R right) {
        super(left, right);
    }

    @Override
    public boolean isConstant() {
        return leftChild().isConstant() && rightChild().isConstant();
    }
}
