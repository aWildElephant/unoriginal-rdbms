package fr.awildelephant.rdbms.execution.operator.accumulator.wrapper;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.execution.operator.accumulator.Accumulator;

public class AccumulatorWithInputWrapper implements AccumulatorWrapper {

    private final Accumulator accumulator;
    private final int inputColumnIndex;

    public AccumulatorWithInputWrapper(Accumulator accumulator, int inputColumnIndex) {
        this.accumulator = accumulator;
        this.inputColumnIndex = inputColumnIndex;
    }

    @Override
    public void accumulate(Record record) {
        accumulator.accumulate(record.get(inputColumnIndex));
    }

    @Override
    public DomainValue result() {
        return accumulator.result();
    }
}
