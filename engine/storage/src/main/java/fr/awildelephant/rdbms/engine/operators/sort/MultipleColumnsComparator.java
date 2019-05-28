package fr.awildelephant.rdbms.engine.operators.sort;

import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.schema.Column;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class MultipleColumnsComparator implements RecordComparator {

    private final List<RecordComparator> comparators;

    public MultipleColumnsComparator(Schema schema, List<ColumnName> columns) {
        comparators = columns.stream().map(columnReference -> {
            final Column column = schema.column(columnReference.name());

            switch (column.domain()) {
                case INTEGER:
                    return new IntegerColumnComparator(column.index());
                case TEXT:
                    return new TextColumnComparator(column.index());
                default:
                    throw new UnsupportedOperationException("Sort on something else than a text column");
            }
        }).collect(toList());
    }

    public int compare(Record record, Record reference) {
        for (RecordComparator comparator : comparators) {
            final int comparison = comparator.compare(record, reference);

            if (comparison != 0) {
                return comparison;
            }
        }

        return 0;
    }
}
