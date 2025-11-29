package fr.awildelephant.rdbms.execution.operator.sort;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.execution.operator.sort.comparator.DateColumnComparator;
import fr.awildelephant.rdbms.execution.operator.sort.comparator.DecimalColumnComparator;
import fr.awildelephant.rdbms.execution.operator.sort.comparator.DomainValueComparator;
import fr.awildelephant.rdbms.execution.operator.sort.comparator.IntegerColumnComparator;
import fr.awildelephant.rdbms.execution.operator.sort.comparator.LongColumnComparator;
import fr.awildelephant.rdbms.execution.operator.sort.comparator.NullsMaxComparator;
import fr.awildelephant.rdbms.execution.operator.sort.comparator.NullsMinComparator;
import fr.awildelephant.rdbms.execution.operator.sort.comparator.TextColumnComparator;
import fr.awildelephant.rdbms.operator.logical.sort.SortSpecification;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.OrderedColumnMetadata;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class RecordComparatorFactory {

    public static RecordComparator buildComparator(Schema schema, List<SortSpecification> sortSpecificationList) {
        if (sortSpecificationList.size() == 1) {
            return buildSingleColumnComparator(schema, sortSpecificationList.getFirst());
        }

        final List<SingleColumnComparator> comparators = new ArrayList<>(sortSpecificationList.size());
        for (SortSpecification sortSpecification : sortSpecificationList) {
            comparators.add(buildSingleColumnComparator(schema, sortSpecification));
        }

        return new MultipleColumnsComparator(comparators);
    }

    private static SingleColumnComparator buildSingleColumnComparator(Schema schema, SortSpecification sortSpecification) {
        final OrderedColumnMetadata column = schema.column(sortSpecification.column());
        Comparator<DomainValue> domainValueComparator = comparatorForDomain(column);

        if (!column.metadata().notNull()) {
            if (sortSpecification.nullsLast()) {
                domainValueComparator = new NullsMaxComparator(domainValueComparator);
            } else {
                domainValueComparator = new NullsMinComparator(domainValueComparator);
            }
        }

        if (!sortSpecification.ascending()) {
            domainValueComparator = domainValueComparator.reversed();
        }

        return new SingleColumnComparator(column.index(), domainValueComparator);
    }

    private static DomainValueComparator comparatorForDomain(OrderedColumnMetadata column) {
        final Domain domain = column.metadata().domain();
        return switch (domain) {
            case DATE -> new DateColumnComparator();
            case DECIMAL -> new DecimalColumnComparator();
            case INTEGER -> new IntegerColumnComparator();
            case LONG -> new LongColumnComparator();
            case TEXT -> new TextColumnComparator();
            default -> throw new UnsupportedOperationException("Unsupported sort on " + domain + " column");
        };
    }
}
