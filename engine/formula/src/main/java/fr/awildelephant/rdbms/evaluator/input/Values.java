package fr.awildelephant.rdbms.evaluator.input;

import fr.awildelephant.rdbms.data.value.DomainValue;

public interface Values {

    DomainValue valueOf(int index);
}
