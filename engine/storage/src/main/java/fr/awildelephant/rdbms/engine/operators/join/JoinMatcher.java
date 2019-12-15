package fr.awildelephant.rdbms.engine.operators.join;

import fr.awildelephant.rdbms.engine.data.record.Record;

import java.util.List;

public interface JoinMatcher {

    List<Record> match(Record leftRecord);
}
