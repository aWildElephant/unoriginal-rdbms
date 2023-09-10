package fr.awildelephant.rdbms.execution.operator.values;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.input.Values;
import fr.awildelephant.rdbms.storage.data.record.Record;

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
