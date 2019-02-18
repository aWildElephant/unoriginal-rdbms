package fr.awildelephant.rdbms.evaluator;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.Reference;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.Map;

public class Formula {

    private final Operation operation;
    private final Map<String, Reference> references;
    private final String outputName;

    public Formula(Operation operation, Map<String, Reference> references, String outputName) {
        this.operation = operation;
        this.references = references;
        this.outputName = outputName;
    }

    public DomainValue evaluate(Values values) {
        setValues(values);

        return operation.evaluate();
    }

    private void setValues(Values values) {
        references.forEach((name, reference) -> reference.set(values.valueOf(name)));
    }

    public String outputName() {
        return outputName;
    }

    public Domain outputType() {
        return operation.domain();
    }
}
