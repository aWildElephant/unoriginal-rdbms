package fr.awildelephant.rdbms.execution.operator.join;

import fr.awildelephant.rdbms.storage.data.chunk.Chunk;
import fr.awildelephant.rdbms.storage.data.record.Record;

public interface JoinMatcher {

    Chunk<Record> match(Record leftRecord);
}
