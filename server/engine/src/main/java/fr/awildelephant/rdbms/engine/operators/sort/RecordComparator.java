package fr.awildelephant.rdbms.engine.operators.sort;

import fr.awildelephant.rdbms.engine.data.record.Record;

import java.util.Comparator;

public interface RecordComparator extends Comparator<Record> {

    int compare(Record record, Record reference);
}
