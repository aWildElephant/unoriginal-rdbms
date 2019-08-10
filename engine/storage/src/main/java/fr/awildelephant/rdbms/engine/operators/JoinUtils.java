package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;

final class JoinUtils {

    private JoinUtils() {

    }

    static Record joinRecords(Record leftRecord, Record rightRecord) {
        final int numberOfColumnsFromLeftTable = leftRecord.size();

        final DomainValue[] values = new DomainValue[numberOfColumnsFromLeftTable + rightRecord.size()];

        for (int i = 0; i < numberOfColumnsFromLeftTable; i++) {
            values[i] = leftRecord.get(i);
        }

        for (int i = 0; i < rightRecord.size(); i++) {
            values[numberOfColumnsFromLeftTable + i] = rightRecord.get(i);
        }


        return new Record(values);
    }
}
