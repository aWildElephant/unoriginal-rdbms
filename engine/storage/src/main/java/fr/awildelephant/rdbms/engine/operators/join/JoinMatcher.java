package fr.awildelephant.rdbms.engine.operators.join;

import fr.awildelephant.rdbms.engine.data.chunk.Chunk;
import fr.awildelephant.rdbms.engine.data.record.Record;

public interface JoinMatcher {

    Chunk<Record> match(Record leftRecord);
}
