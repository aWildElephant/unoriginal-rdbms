package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

public interface Operation {

    DomainValue evaluate();

    Domain domain();

    boolean isConstant();
}
