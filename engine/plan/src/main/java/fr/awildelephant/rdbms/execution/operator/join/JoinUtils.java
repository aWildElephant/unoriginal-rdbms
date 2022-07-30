package fr.awildelephant.rdbms.execution.operator.join;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.record.Tuple;

public final class JoinUtils {

    private JoinUtils() {

    }

    public static Tuple joinRecords(Record leftRecord, Record rightRecord) {
        final int numberOfColumnsFromLeftTable = leftRecord.size();

        final DomainValue[] values = new DomainValue[numberOfColumnsFromLeftTable + rightRecord.size()];

        for (int i = 0; i < numberOfColumnsFromLeftTable; i++) {
            values[i] = leftRecord.get(i);
        }

        for (int i = 0; i < rightRecord.size(); i++) {
            values[numberOfColumnsFromLeftTable + i] = rightRecord.get(i);
        }


        return new Tuple(values);
    }
}
