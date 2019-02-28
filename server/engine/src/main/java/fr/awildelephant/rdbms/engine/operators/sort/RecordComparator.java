package fr.awildelephant.rdbms.engine.operators.sort;

import fr.awildelephant.rdbms.engine.data.record.Record;

public interface RecordComparator {

    int compare(Record record, Record reference);
}
