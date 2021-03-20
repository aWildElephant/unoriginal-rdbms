package fr.awildelephant.rdbms.engine.operators.join;

import fr.awildelephant.rdbms.engine.data.record.Record;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.engine.operators.JoinUtils.joinRecords;

public final class FlippedInnerJoinOutputCreator implements JoinOutputCreator {

    @Override
    public List<Record> join(Record leftPart, List<Record> rightParts) {
        if (rightParts.isEmpty()) {
            return List.of();
        }

        final List<Record> output = new ArrayList<>(rightParts.size());

        for (Record rightPart : rightParts) {
            output.add(joinRecords(rightPart, leftPart));
        }

        return output;
    }
}
