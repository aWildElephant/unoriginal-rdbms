package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;

public class BreakdownOperator implements Operator<Table, Stream<Table>> {

    private final List<String> breakdowns;

    public BreakdownOperator(List<String> breakdowns) {
        this.breakdowns = breakdowns;
    }

    @Override
    public Stream<Table> compute(Table inputTable) {
        final Schema schema = inputTable.schema();

        final int[] projectionOnBreakdowns = buildProjectionOnBreakdowns(schema, breakdowns);

        int counter = 0;
        final Map<List<DomainValue>, Integer> mapping = new HashMap<>();
        final List<Table> buckets = new ArrayList<>();

        for (Record record : inputTable) {
            final List<DomainValue> values = projectOnBreakdowns(record, projectionOnBreakdowns);

            final Integer bucketIndex = mapping.get(values);

            Table bucket;
            if (bucketIndex == null) {
                bucket = simpleTable(schema);
                buckets.add(bucket);
                mapping.put(values, counter++);
            } else {
                bucket = buckets.get(bucketIndex);
            }

            bucket.add(record);
        }

        return buckets.stream();
    }

    private static int[] buildProjectionOnBreakdowns(Schema schema, List<String> breakdowns) {
        final int[] thing = new int[breakdowns.size()];

        for (int i = 0; i < breakdowns.size(); i++) {
            thing[i] = schema.indexOf(breakdowns.get(i));
        }

        return thing;
    }

    private List<DomainValue> projectOnBreakdowns(Record record, int[] thing) {
        final List<DomainValue> projection = new ArrayList<>(thing.length);

        for (int index : thing) {
            projection.add(record.get(index));
        }

        return projection;
    }
}
