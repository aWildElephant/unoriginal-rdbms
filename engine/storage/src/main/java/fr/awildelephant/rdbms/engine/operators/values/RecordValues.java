package fr.awildelephant.rdbms.engine.operators.values;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.evaluator.Values;

public class RecordValues implements Values {

    private Record record;

    public void setRecord(Record record) {
        this.record = record;
    }

    @Override
    public DomainValue valueOf(int index) {
        return record.get(index);
    }
}
