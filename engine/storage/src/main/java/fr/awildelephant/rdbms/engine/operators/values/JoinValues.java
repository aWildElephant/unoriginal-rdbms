package fr.awildelephant.rdbms.engine.operators.values;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.evaluator.Values;

public class JoinValues implements Values {

    private Record leftRecord;
    private Record rightRecord;
    private int numberOfColumnsFromLeftInput;

    public JoinValues(int numberOfColumnsFromLeftInput) {
        this.numberOfColumnsFromLeftInput = numberOfColumnsFromLeftInput;

    }

    public void setRecords(Record leftRecord, Record rightRecord) {
        this.leftRecord = leftRecord;
        this.rightRecord = rightRecord;
    }

    @Override
    public DomainValue valueOf(int index) {
        if (index < numberOfColumnsFromLeftInput) {
            return leftRecord.get(index);
        }

        return rightRecord.get(index - numberOfColumnsFromLeftInput);
    }
}
