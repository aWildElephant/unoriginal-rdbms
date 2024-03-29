package fr.awildelephant.rdbms.execution.operator.sort;

import fr.awildelephant.rdbms.storage.data.record.Record;

import java.util.Comparator;

public interface RecordComparator extends Comparator<Record> {

    int compare(Record record, Record reference);
}
