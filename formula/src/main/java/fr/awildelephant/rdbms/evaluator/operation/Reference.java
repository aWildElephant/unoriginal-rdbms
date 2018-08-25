package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

public class Reference implements Operation {

    private final Domain domain;

    private DomainValue value;

    private Reference(Domain domain) {
        this.domain = domain;
    }

    public static Reference reference(Domain domain) {
        return new Reference(domain);
    }

    public void set(DomainValue value) {
        this.value = value;
    }

    @Override
    public DomainValue evaluate() {
        return value;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public Domain domain() {
        return domain;
    }
}
