package fr.awildelephant.rdbms.evaluator;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.Reference;

public class Formula {

    private final Operation operation;
    private final Reference[] references;

    public Formula(Operation operation, Reference[] references) {
        this.operation = operation;
        this.references = references;
    }

    public DomainValue evaluate(Values values) {
        setValues(values);

        return operation.evaluate();
    }

    private void setValues(Values values) {
        for (int i = 0; i < references.length; i++) {
            final Reference reference = references[i];
            if (reference != null) {
                reference.set(values.valueOf(i));
            }
        }
    }
}
