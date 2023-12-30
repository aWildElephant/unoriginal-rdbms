package fr.awildelephant.rdbms.evaluator.operation.text;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.Operation;

import java.util.Collection;
import java.util.List;

public final class SubstringOperation implements TextOperation {

    private final TextOperation input;
    // TODO: start & length devraient évaluer comme un entier
    private final Operation start;
    private final Operation length;

    private SubstringOperation(TextOperation input, Operation start, Operation length) {
        this.input = input;
        this.start = start;
        this.length = length;
    }

    public static SubstringOperation substringOperation(TextOperation input, Operation start, Operation length) {
        return new SubstringOperation(input, start, length);
    }

    @Override
    public String evaluateString() {
        final String inputValue = input.evaluateString();
        if (inputValue == null || inputValue.isEmpty()) {
            return inputValue;
        }

        final DomainValue startValue = start.evaluateAndWrap();
        if (startValue.isNull()) {
            return null;
        }

        final DomainValue lengthValue = length.evaluateAndWrap();
        if (lengthValue.isNull()) {
            return null;
        }

        final int startIndex = startValue.getInt() - 1;
        final int endIndex = Math.min(startIndex + lengthValue.getInt(), inputValue.length());

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
