package fr.awildelephant.rdbms.engine.operators.join;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.engine.operators.JoinUtils.joinRecords;

public final class LeftJoinOutputCreator implements JoinOutputCreator {

    private final Schema leftSchema;
    private final Schema rightSchema;

    public LeftJoinOutputCreator(Schema leftSchema, Schema rightSchema) {
        this.leftSchema = leftSchema;
        this.rightSchema = rightSchema;
    }

    @Override
    public List<Record> join(Record leftPart, List<Record> rightParts) {
        if (rightParts == null || rightParts.isEmpty()) {
            return unmatched(leftPart);
        }

        final List<Record> output = new ArrayList<>(rightParts.size());

        for (Record rightPart : rightParts) {
            output.add(joinRecords(leftPart, rightPart));
        }

        return output;
    }

    private List<Record> unmatched(Record leftPart) {
        final int numberOfColumnsFromLeftTable = leftSchema.numberOfAttributes();
        final int numberOfColumnsFromRightTable = rightSchema.numberOfAttributes();

        final DomainValue[] values = new DomainValue[numberOfColumnsFromLeftTable + numberOfColumnsFromRightTable];

        for (int i = 0; i < numberOfColumnsFromLeftTable; i++) {
            values[i] = leftPart.get(i);
        }

        for (int i = 0; i < numberOfColumnsFromRightTable; i++) {
            values[numberOfColumnsFromLeftTable + i] = nullValue();
        }

        return List.of(new Record(values));
    }
}
