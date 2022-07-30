package fr.awildelephant.rdbms.execution.operator.accumulator;

import fr.awildelephant.rdbms.data.value.DomainValue;

public interface Accumulator {

    void accumulate(DomainValue value);

    DomainValue result();
}
