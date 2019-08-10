package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

import java.util.stream.Stream;

public interface ValueExpression {

    Domain domain();

    Stream<String> variables();

    <T> T accept(ValueExpressionVisitor<T> visitor);
}
