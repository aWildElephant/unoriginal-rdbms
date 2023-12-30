package fr.awildelephant.rdbms.evaluator;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.input.Values;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.ValuesHolder;

public class Formula {

    private final Operation operation;
    private final ValuesHolder valuesHolder;

    public Formula(Operation operation, ValuesHolder valuesHolder) {
        this.operation = operation;
        this.valuesHolder = valuesHolder;
    }

    public DomainValue evaluate(Values values) {
        setValues(values);

        return operation.evaluateAndWrap();
    }

    private void setValues(Values values) {
        valuesHolder.setValues(values);
    }
}
