package fr.awildelephant.rdbms.plan.arithmetic;

import java.util.Iterator;
import java.util.Optional;

import static fr.awildelephant.rdbms.plan.arithmetic.AndExpression.andExpression;

public final class FilterCollapser {

    private FilterCollapser() {

    }

    public static Optional<ValueExpression> collapseFilters(Iterable<ValueExpression> filters) {
        final Iterator<ValueExpression> filtersIterator = filters.iterator();

        if (!filtersIterator.hasNext()) {
            return Optional.empty();
        }

        ValueExpression collapsedFilters = filtersIterator.next();

        while (filtersIterator.hasNext()) {
            collapsedFilters = andExpression(collapsedFilters, filtersIterator.next());
        }

        return Optional.of(collapsedFilters);
    }
}
