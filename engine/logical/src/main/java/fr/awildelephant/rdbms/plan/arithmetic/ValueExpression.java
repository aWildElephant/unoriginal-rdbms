package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.function.Function;
import java.util.stream.Stream;

public interface ValueExpression {

    Domain domain();

    Stream<ColumnReference> variables();

    ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer);

    <T> T accept(ValueExpressionVisitor<T> visitor);
}
