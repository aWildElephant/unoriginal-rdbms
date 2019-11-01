package fr.awildelephant.rdbms.plan.arithmetic;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FilterExpander extends DefaultValueExpressionVisitor<Stream<ValueExpression>> {

    private static final FilterExpander INSTANCE = new FilterExpander();

    private FilterExpander() {

    }

    public static List<ValueExpression> expandFilters(ValueExpression expression) {
        return INSTANCE.apply(expression).collect(Collectors.toList());
    }

    @Override
    public Stream<ValueExpression> visit(AndExpression and) {
        return Stream.concat(apply(and.left()), apply(and.right()));
    }

    @Override
    protected Stream<ValueExpression> defaultVisit(ValueExpression expression) {
        return Stream.of(expression);
    }
}
