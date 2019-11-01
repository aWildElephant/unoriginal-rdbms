package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;
import static fr.awildelephant.rdbms.engine.operators.JoinUtils.joinRecords;

public class InnerHashJoinOperator implements JoinOperator {

    private final int[] leftMapping;
    private final int[] rightMapping;
    private final Schema outputSchema;

    public InnerHashJoinOperator(int[] leftMapping, int[] rightMapping, Schema outputSchema) {
        this.leftMapping = leftMapping;
        this.rightMapping = rightMapping;
        this.outputSchema = outputSchema;
    }

    @Override
    public Table compute(Table left, Table right) {
        final Map<Record, List<Record>> hash = hash(right, rightMapping);

        final Table outputTable = simpleTable(outputSchema);

        for (Record record : left) {
            final List<Record> matchingRecords = hash.get(key(record, leftMapping));
            if (matchingRecords != null) {
                for (Record matchingRecord : matchingRecords) {
                    outputTable.add(joinRecords(record, matchingRecord));
                }
            }
        }

        return outputTable;
    }

    private Map<Record, List<Record>> hash(Table table, int[] mapping) {
        final Map<Record, List<Record>> hash = new HashMap<>();

        for (Record record : table) {
            final Record key = key(record, mapping);
            hash.computeIfAbsent(key, ignored -> new ArrayList<>())
                .add(record);
        }

        return hash;
    }

    private Record key(Record record, int[] mapping) {
        final DomainValue[] values = new DomainValue[mapping.length];

        for (int i = 0; i < mapping.length; i++) {
            values[i] = record.get(mapping[i]);
        }

        return new Record(values);
    }
}
