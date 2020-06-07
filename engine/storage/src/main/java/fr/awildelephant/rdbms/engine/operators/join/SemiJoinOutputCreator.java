package fr.awildelephant.rdbms.engine.operators.join;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;

import java.util.List;

import static fr.awildelephant.rdbms.data.value.FalseValue.falseValue;
import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;

public final class SemiJoinOutputCreator implements JoinOutputCreator {

    public static final SemiJoinOutputCreator INSTANCE = new SemiJoinOutputCreator();

    private SemiJoinOutputCreator() {

    }

    @Override
    public List<Record> join(Record leftPart, List<Record> rightParts) {
        final DomainValue[] values = new DomainValue[leftPart.size() + 1];

        for (int index = 0; index < leftPart.size(); index++) {
            values[index] = leftPart.get(index);
        }

        if (rightParts.isEmpty()) {
            values[values.length - 1] = falseValue();
        } else {
            values[values.length - 1] = trueValue();
        }

        return List.of(new Record(values));
    }
}
