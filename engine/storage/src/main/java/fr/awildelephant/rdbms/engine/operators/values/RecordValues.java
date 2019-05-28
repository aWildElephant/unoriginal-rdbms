package fr.awildelephant.rdbms.engine.operators.values;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.evaluator.Values;
import fr.awildelephant.rdbms.schema.Schema;

public class RecordValues implements Values {

    private final Schema inputSchema;

    private Record record;

    public RecordValues(Schema inputSchema) {
        this.inputSchema = inputSchema;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    @Override
    public DomainValue valueOf(String identifier) {
        return record.get(inputSchema.indexOf(identifier));
    }
}
