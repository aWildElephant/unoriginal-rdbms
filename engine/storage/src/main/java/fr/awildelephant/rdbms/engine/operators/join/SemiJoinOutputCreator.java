package fr.awildelephant.rdbms.engine.operators.join;

import fr.awildelephant.rdbms.engine.data.record.Record;

import java.util.List;

public final class SemiJoinOutputCreator implements JoinOutputCreator {

    public static final SemiJoinOutputCreator INSTANCE = new SemiJoinOutputCreator();

    private SemiJoinOutputCreator() {

    }

    @Override
    public List<Record> join(Record leftPart, List<Record> rightParts) {
        if (rightParts.isEmpty()) {
            return List.of();
        } else {
            return List.of(leftPart);
        }
    }
}
