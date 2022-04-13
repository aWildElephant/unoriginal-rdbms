package fr.awildelephant.rdbms.engine.operators.join;

import fr.awildelephant.rdbms.engine.data.chunk.BuildableChunk;
import fr.awildelephant.rdbms.engine.data.chunk.Chunk;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.values.JoinValues;
import fr.awildelephant.rdbms.evaluator.Formula;

public final class NestedLoopJoinMatcher implements JoinMatcher {

    private final Table rightTable;
    private final Formula joinSpecification;

    public NestedLoopJoinMatcher(Table rightTable, Formula joinSpecification) {
        this.rightTable = rightTable;
        this.joinSpecification = joinSpecification;
    }

    @Override
    public Chunk<Record> match(Record leftRecord) {
        final JoinValues values = new JoinValues(leftRecord);

        final BuildableChunk<Record> matchingRightRecords = new BuildableChunk<>();
        for (Record rightRecord : rightTable) {
            values.setRightRecord(rightRecord);
            if (joinSpecification.evaluate(values).getBool()) {
                matchingRightRecords.add(rightRecord.materialize());
            }
        }

        return matchingRightRecords;
    }
}
