package fr.awildelephant.rdbms.engine.operators.accumulator.wrapper;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.operators.accumulator.Accumulator;

import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;

public class AccumulatorWithNoInputWrapper implements AccumulatorWrapper {

    private final Accumulator accumulator;

    public AccumulatorWithNoInputWrapper(Accumulator accumulator) {
        this.accumulator = accumulator;
    }

    @Override
    public void accumulate(Record record) {
        accumulator.accumulate(nullValue());
    }

    @Override
    public DomainValue result() {
        return accumulator.result();
    }
}
