package fr.awildelephant.rdbms.execution.operator.values;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.evaluator.input.Values;

public final class JoinValues implements Values {

    private final Record leftRecord;
    private final int numberOfColumnsFromLeftInput;

    private Record rightRecord;

    public JoinValues(Record leftRecord) {
        this.leftRecord = leftRecord;
        this.numberOfColumnsFromLeftInput = leftRecord.size();
    }

    public void setRightRecord(Record rightRecord) {
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
