package fr.awildelephant.rdbms.engine.operators.accumulator.wrapper;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;

public interface AccumulatorWrapper {

    void accumulate(Record record);

    DomainValue result();
}
