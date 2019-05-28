package fr.awildelephant.rdbms.evaluator;

import fr.awildelephant.rdbms.data.value.DomainValue;

public interface Values {

    DomainValue valueOf(String identifier);
}
