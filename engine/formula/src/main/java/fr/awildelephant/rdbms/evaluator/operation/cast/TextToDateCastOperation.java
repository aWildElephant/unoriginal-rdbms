package fr.awildelephant.rdbms.evaluator.operation.cast;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.date.DateOperation;
import fr.awildelephant.rdbms.evaluator.operation.text.TextOperation;
import fr.awildelephant.rdbms.tree.UnaryNode;

import java.time.LocalDate;

public final class TextToDateCastOperation extends UnaryNode<Operation, TextOperation> implements DateOperation {

    public TextToDateCastOperation(final TextOperation input) {
        super(input);
    }

    @Override
    public LocalDate evaluateLocalDate() {
        final String childValue = child().evaluateString();

        if (childValue == null) {
            return null;
        }

        return LocalDate.parse(childValue);
    }

    @Override
    public boolean isConstant() {
        return child().isConstant();
    }
}
