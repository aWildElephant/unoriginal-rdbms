package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.tree.LeafNode;

import java.util.function.Supplier;

public class Reference extends LeafNode<Operation> implements Operation {

    private final Domain domain;
    private final Supplier<DomainValue> valueSupplier;

    private Reference(Domain domain, Supplier<DomainValue> valueSupplier) {
        this.domain = domain;
        this.valueSupplier = valueSupplier;
    }

    public static Reference reference(Domain domain, Supplier<DomainValue> valueSupplier) {
        return new Reference(domain, valueSupplier);
    }

    @Override
    public DomainValue evaluateAndWrap() {
        return valueSupplier.get();
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
