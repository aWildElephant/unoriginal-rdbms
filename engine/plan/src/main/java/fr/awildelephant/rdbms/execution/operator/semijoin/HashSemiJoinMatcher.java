package fr.awildelephant.rdbms.execution.operator.semijoin;

import fr.awildelephant.rdbms.execution.operator.hashing.HashingHelper;
import fr.awildelephant.rdbms.storage.data.record.Record;
import fr.awildelephant.rdbms.storage.data.table.Table;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import java.util.HashSet;
import java.util.Set;

import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.TRUE;

public final class HashSemiJoinMatcher implements SemiJoinMatcher {

    private final int[] leftMapping;
    private final Set<Record> hash;

    public HashSemiJoinMatcher(Table rightTable, int[] leftMapping, int[] rightMapping) {
        this.leftMapping = leftMapping;
        this.hash = hash(rightTable, rightMapping);
    }

    private static Set<Record> hash(Table table, int[] mapping) {
        final Set<Record> hash = new HashSet<>();

        for (Record record : table) {
            hash.add(key(record, mapping));
        }

        return hash;
    }

    private static Record key(Record record, int[] mapping) {
        return HashingHelper.key(record, mapping);
    }

    @Override
    public ThreeValuedLogic match(Record leftRecord) {
        return hash.contains(key(leftRecord, leftMapping)) ? TRUE : FALSE;
    }
}
