package fr.awildelephant.rdbms.execution.operator.sort;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.execution.sort.SortSpecification;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.OrderedColumnMetadata;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public final class MultipleColumnsComparator implements RecordComparator {

    private final List<Comparator<Record>> comparators;

    public MultipleColumnsComparator(Schema schema, List<SortSpecification> sortSpecificationList) {
        comparators = sortSpecificationList.stream().map(sortSpecification -> {
            final OrderedColumnMetadata column = schema.column(sortSpecification.column());

            final RecordComparator comparator = comparatorForDomain(column);

            return sortSpecification.ascending() ? comparator : comparator.reversed();
        }).collect(toList());
    }

    private RecordComparator comparatorForDomain(OrderedColumnMetadata column) {
        final Domain domain = column.metadata().domain();
        return switch (domain) {
            case DATE -> new DateColumnComparator(column.index());
            case DECIMAL -> new DecimalColumnComparator(column.index());
            case INTEGER -> new IntegerColumnComparator(column.index());
            case TEXT -> new TextColumnComparator(column.index());
            default -> throw new UnsupportedOperationException("Unsupported sort on " + domain + " column");
        };
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
