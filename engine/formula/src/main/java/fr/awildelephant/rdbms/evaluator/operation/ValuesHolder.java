package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.Values;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.evaluator.operation.Reference.reference;

public final class ValuesHolder implements Values {

    private Values values;

    public void setValues(Values values) {
        this.values = values;
    }

    @Override
    public DomainValue valueOf(int index) {
        return values.valueOf(index);
    }

    public Reference createReference(int index, Domain domain) {
        return reference(domain, () -> values.valueOf(index));
    }
}
