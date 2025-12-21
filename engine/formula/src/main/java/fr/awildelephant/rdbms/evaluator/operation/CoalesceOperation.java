package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.List;

import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;

public record CoalesceOperation(List<Operation> children) implements Operation {

    @Override
    public DomainValue evaluateAndWrap() {
        for (Operation child : children) {
            final DomainValue childValue = child.evaluateAndWrap();

            if (!childValue.isNull()) {
                return childValue;
            }
        }
        return nullValue();
    }

    @Override
    public Domain domain() {
        return children.getFirst().domain();
    }

    @Override
    public boolean isConstant() {
        // TODO: a value is constant and not null, we can remove anything after it. If it's null we can remove it
        return children.stream().allMatch(Operation::isConstant);
    }
}
