package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.tree.Tree;

public interface Operation extends Tree<Operation> {

    DomainValue evaluateAndWrap();

    Domain domain();

    boolean isConstant();
}
