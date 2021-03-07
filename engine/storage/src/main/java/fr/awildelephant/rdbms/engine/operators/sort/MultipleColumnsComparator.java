package fr.awildelephant.rdbms.engine.operators.sort;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.plan.sort.SortSpecification;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MultipleColumnsComparator implements RecordComparator {

    private final List<Comparator<Record>> comparators;

    public MultipleColumnsComparator(Schema schema, List<SortSpecification> sortSpecificationList) {
        comparators = sortSpecificationList.stream().map(sortSpecification -> {
            final ColumnMetadata column = schema.column(sortSpecification.column());

            final RecordComparator comparator = comparatorForDomain(column);

            return sortSpecification.ascending() ? comparator : comparator.reversed();
        }).collect(toList());
    }

    private RecordComparator comparatorForDomain(ColumnMetadata column) {
        switch (column.domain()) {
            case DATE:
                return new DateColumnComparator(column.index());
            case DECIMAL:
                return new DecimalColumnComparator(column.index());
            case INTEGER:
                return new IntegerColumnComparator(column.index());
            case TEXT:
                return new TextColumnComparator(column.index());
            default:
                throw new UnsupportedOperationException("Sort on something else than a text column");
        }
    }

    public int compare(Record record, Record reference) {
        for (Comparator<Record> comparator : comparators) {
            final int comparison = comparator.compare(record, reference);

            if (comparison != 0) {
                return comparison;
            }
        }

        return 0;
    }
}
