package fr.awildelephant.rdbms.engine.operators.hashing;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.record.Tuple;
import fr.awildelephant.rdbms.engine.data.table.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class HashingHelper {

    private HashingHelper() {

    }

    public static Map<Record, List<Record>> hash(Table table, int[] mapping) {
        final Map<Record, List<Record>> hash = new HashMap<>();

        for (Record record : table) {
            final Record tuple = record.materialize();
            final Record key = key(tuple, mapping);
            hash.computeIfAbsent(key, ignored -> new ArrayList<>()).add(tuple);
        }

        return hash;
    }

    public static Record key(Record record, int[] mapping) {
        final DomainValue[] values = new DomainValue[mapping.length];

        for (int i = 0; i < mapping.length; i++) {
            values[i] = record.get(mapping[i]);
        }

        return new Tuple(values);
    }
}
