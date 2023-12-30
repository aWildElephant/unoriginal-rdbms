package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.tree.UnaryNode;

public abstract class UnaryOperation<C extends Operation> extends UnaryNode<Operation, C> implements Operation {

    protected UnaryOperation(C child) {
        super(child);
    }

    @Override
    public boolean isConstant() {
        return child().isConstant();
    }
}
