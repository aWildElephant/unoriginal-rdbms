package fr.awildelephant.rdbms.execution.operator.join;

import fr.awildelephant.rdbms.engine.data.chunk.Chunk;
import fr.awildelephant.rdbms.engine.data.record.Record;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.execution.operator.join.JoinUtils.joinRecords;

public final class InnerJoinOutputCreator implements JoinOutputCreator {

    @Override
    public List<Record> join(Record leftPart, Chunk<Record> rightParts) {
        if (rightParts.isEmpty()) {
            return List.of();
        }

        final List<Record> output = new ArrayList<>(rightParts.size());

        for (Record rightPart : rightParts.content()) {
            output.add(joinRecords(leftPart, rightPart));
        }

        return output;
    }
}
