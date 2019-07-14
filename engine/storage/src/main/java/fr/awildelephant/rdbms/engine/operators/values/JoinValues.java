package fr.awildelephant.rdbms.engine.operators.values;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.evaluator.Values;
import fr.awildelephant.rdbms.schema.Schema;

public class JoinValues implements Values {

    private final Schema leftSchema;
    private final Schema rightSchema;

    private Record leftRecord;
    private Record rightRecord;

    public JoinValues(Schema leftSchema, Schema rightSchema) {
        this.leftSchema = leftSchema;
        this.rightSchema = rightSchema;
    }

    public void setRecords(Record leftRecord, Record rightRecord) {
        this.leftRecord = leftRecord;
        this.rightRecord = rightRecord;
    }

    @Override
    public DomainValue valueOf(String identifier) {
        if (leftSchema.contains(identifier)) {
            return leftRecord.get(leftSchema.indexOf(identifier));
        }

        return rightRecord.get(rightSchema.indexOf(identifier));
    }
}
