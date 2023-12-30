package fr.awildelephant.rdbms.evaluator.operation.text;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.tree.LeafNode;

import java.util.function.Supplier;

public final class TextVariable extends LeafNode<Operation> implements TextOperation {

    private final Supplier<String> supplier;

    public TextVariable(final Supplier<String> supplier) {
        this.supplier = supplier;
    }

    @Override
    public String evaluateString() {
        return supplier.get();
    }

    @Override
    public boolean isConstant() {
        return false;
    }
}
