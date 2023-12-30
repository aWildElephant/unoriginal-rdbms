package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.tree.LeafNode;

public abstract class ConstantOperation extends LeafNode<Operation> implements Operation {

    @Override
    public boolean isConstant() {
        return true;
    }
}
