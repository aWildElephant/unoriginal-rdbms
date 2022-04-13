package fr.awildelephant.rdbms.engine.operators.accumulator;

import fr.awildelephant.rdbms.data.value.DomainValue;

public interface Accumulator {

    void accumulate(DomainValue value);

    DomainValue result();
}
