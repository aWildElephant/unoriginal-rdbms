package fr.awildelephant.rdbms.evaluator;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.Reference;

import java.util.Map;

public class Formula {

    private final Operation operation;
    private final Map<String, Reference> references;

    public Formula(Operation operation, Map<String, Reference> references) {
        this.operation = operation;
        this.references = references;
    }

    public DomainValue evaluate(Values values) {
        setValues(values);

        return operation.evaluate();
    }

    private void setValues(Values values) {
        references.forEach((name, reference) -> reference.set(values.valueOf(name)));
    }
}
