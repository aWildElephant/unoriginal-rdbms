package fr.awildelephant.rdbms.engine.operators.join;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class HashJoinMatcher implements JoinMatcher {

    private final int[] leftMapping;
    private final Map<Record, List<Record>> hash;

    public HashJoinMatcher(Table rightTable, int[] leftMapping, int[] rightMapping) {
        this.leftMapping = leftMapping;
        this.hash = hash(rightTable, rightMapping);
    }

    private static Map<Record, List<Record>> hash(Table table, int[] mapping) {
        final Map<Record, List<Record>> hash = new HashMap<>();

        for (Record record : table) {
            final Record key = key(record, mapping);
            hash.computeIfAbsent(key, ignored -> new ArrayList<>())
                .add(record);
        }

        return hash;
    }

    private static Record key(Record record, int[] mapping) {
        final DomainValue[] values = new DomainValue[mapping.length];

        for (int i = 0; i < mapping.length; i++) {
            values[i] = record.get(mapping[i]);
        }

        return new Record(values);
    }

    @Override
    public List<Record> match(Record leftRecord) {
        final List<Record> match = hash.get(key(leftRecord, leftMapping));

        if (match == null) {
            return List.of();
        }

        return match;
    }
}
