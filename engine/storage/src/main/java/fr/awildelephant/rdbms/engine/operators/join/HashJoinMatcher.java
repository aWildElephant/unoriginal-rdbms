package fr.awildelephant.rdbms.engine.operators.join;

import fr.awildelephant.rdbms.engine.data.chunk.Chunk;
import fr.awildelephant.rdbms.engine.data.chunk.ChunkFactory;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;

import java.util.Map;

import static fr.awildelephant.rdbms.engine.operators.hashing.HashingHelper.hash;
import static fr.awildelephant.rdbms.engine.operators.hashing.HashingHelper.key;

public final class HashJoinMatcher implements JoinMatcher {

    private final int[] leftMapping;
    private final Map<Record, ? extends Chunk<Record>> hash;

    public HashJoinMatcher(Table rightTable, int[] leftMapping, int[] rightMapping) {
        this.leftMapping = leftMapping;
        this.hash = hash(rightTable, rightMapping);
    }

    @Override
    public Chunk<Record> match(Record leftRecord) {
        final Chunk<Record> match = hash.get(key(leftRecord, leftMapping));

        if (match == null) {
            return ChunkFactory.of();
        }

        return match;
    }
}
