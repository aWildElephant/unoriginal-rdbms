package fr.awildelephant.rdbms.evaluator;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.Reference;
import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.Map;

public class Formula {

    private final Operation operation;
    private final Map<ColumnReference, Reference> references;

    public Formula(Operation operation, Map<ColumnReference, Reference> references) {
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
