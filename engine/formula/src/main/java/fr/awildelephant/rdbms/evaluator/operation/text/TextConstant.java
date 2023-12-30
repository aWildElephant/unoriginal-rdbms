package fr.awildelephant.rdbms.evaluator.operation.text;

import fr.awildelephant.rdbms.evaluator.operation.ConstantOperation;

public final class TextConstant extends ConstantOperation implements TextOperation {

    private final String value;

    public TextConstant(final String value) {
        this.value = value;
    }

    @Override
    public String evaluateString() {
        return value;
    }
}
