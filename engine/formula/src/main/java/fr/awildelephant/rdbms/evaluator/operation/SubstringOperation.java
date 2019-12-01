package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.data.value.TextValue.textValue;
import static fr.awildelephant.rdbms.schema.Domain.TEXT;

public final class SubstringOperation implements Operation {

    private final Operation input;
    private final Operation start;
    private final Operation length;

    private SubstringOperation(Operation input, Operation start, Operation length) {
        this.input = input;
        this.start = start;
        this.length = length;
    }

    public static SubstringOperation substringOperation(Operation input, Operation start, Operation length) {
        return new SubstringOperation(input, start, length);
    }

    @Override
    public DomainValue evaluate() {
        final DomainValue inputValue = input.evaluate();
        if (inputValue.isNull()) {
            return nullValue();
        }

        final String inputString = inputValue.getString();

        if (inputString.isEmpty()) {
            return inputValue;
        }

        final DomainValue startValue = start.evaluate();
        if (startValue.isNull()) {
            return nullValue();
        }

        final DomainValue lengthValue = length.evaluate();
        if (lengthValue.isNull()) {
            return nullValue();
        }

        final int startIndex = startValue.getInt() - 1;
        final int endIndex = Math.min(startIndex + lengthValue.getInt(), inputString.length());

        return textValue(inputString.substring(startIndex, endIndex));
    }

    @Override
    public Domain domain() {
        return TEXT;
    }

    @Override
    public boolean isConstant() {
        return input.isConstant() && start.isConstant() && length.isConstant();
    }
}
