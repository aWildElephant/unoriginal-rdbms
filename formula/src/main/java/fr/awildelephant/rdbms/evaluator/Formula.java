package fr.awildelephant.rdbms.evaluator;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.Reference;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.Map;
import java.util.Set;

public class Formula {

    private final Operation operation;
    private final Map<String, Reference> references;
    private final String outputName;

    public Formula(Operation operation, Map<String, Reference> references, String outputName) {
        this.operation = operation;
        this.references = references;
        this.outputName = outputName;
    }

    public DomainValue evaluate(Map<String, DomainValue> values) {
        setValues(values);

        return operation.evaluate();
    }

    private void setValues(Map<String, DomainValue> values) {
        references.forEach((name, reference) -> reference.set(values.get(name)));
    }

    public String outputName() {
        return outputName;
    }

    public Domain outputType() {
        return operation.domain();
    }

    public Set<String> requiredColumns() {
        return references.keySet();
    }
}
