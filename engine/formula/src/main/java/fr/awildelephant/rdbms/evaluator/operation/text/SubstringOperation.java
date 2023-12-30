package fr.awildelephant.rdbms.evaluator.operation.text;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.numeric.IntegerOperation;

import java.util.Collection;
import java.util.List;

public final class SubstringOperation implements TextOperation {

    private final TextOperation input;
    private final IntegerOperation start;
    private final IntegerOperation length;

    public SubstringOperation(final TextOperation input, final IntegerOperation start, final IntegerOperation length) {
        this.input = input;
        this.start = start;
        this.length = length;
    }

    @Override
    public String evaluateString() {
        final String inputValue = input.evaluateString();
        if (inputValue == null || inputValue.isEmpty()) {
            return inputValue;
        }

        final Integer startValue = start.evaluateInteger();
        if (startValue == null) {
            return null;
        }

        final Integer lengthValue = length.evaluateInteger();
        if (lengthValue == null) {
            return null;
        }

        final int startIndex = startValue - 1;
        final int endIndex = Math.min(startIndex + lengthValue, inputValue.length());

        return inputValue.substring(startIndex, endIndex);
    }

    @Override
    public boolean isConstant() {
        return input.isConstant() && start.isConstant() && length.isConstant();
    }

    @Override
    public Collection<? extends Operation> children() {
        return List.of(input, start, length);
    }
}
