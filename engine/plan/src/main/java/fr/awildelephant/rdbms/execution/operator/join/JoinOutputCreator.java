package fr.awildelephant.rdbms.execution.operator.join;

import fr.awildelephant.rdbms.engine.data.chunk.Chunk;
import fr.awildelephant.rdbms.engine.data.record.Record;

import java.util.List;

public interface JoinOutputCreator {

    List<Record> join(Record leftPart, Chunk<Record> rightParts);
}