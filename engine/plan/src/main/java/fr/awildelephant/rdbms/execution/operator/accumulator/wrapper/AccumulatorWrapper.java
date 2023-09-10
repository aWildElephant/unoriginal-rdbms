package fr.awildelephant.rdbms.execution.operator.accumulator.wrapper;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.storage.data.record.Record;

public interface AccumulatorWrapper {

    void accumulate(Record record);

    DomainValue result();
}
