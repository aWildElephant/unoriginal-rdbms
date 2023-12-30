package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.tree.LeafNode;

public class Constant extends LeafNode<Operation> implements Operation {

    private final DomainValue value;
    private final Domain domain;

    private Constant(DomainValue value, Domain domain) {
        this.value = value;
        this.domain = domain;
    }

    public static Constant constant(DomainValue value, Domain domain) {
        return new Constant(value, domain);
    }

    @Override
    public DomainValue evaluateAndWrap() {
        return value;
    }

    @Override
    public Domain domain() {
        return domain;
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
