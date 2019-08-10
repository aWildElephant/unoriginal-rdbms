package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;

import java.util.Iterator;
import java.util.Optional;

import static fr.awildelephant.rdbms.plan.arithmetic.AndExpression.andExpression;

final class FilterCollapser {

    private FilterCollapser() {

    }

    static Optional<ValueExpression> collapseFilters(Iterable<ValueExpression> filters) {
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
