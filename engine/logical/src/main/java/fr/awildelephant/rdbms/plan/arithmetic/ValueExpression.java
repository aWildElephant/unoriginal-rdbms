package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.tree.Tree;

import java.util.function.Function;
import java.util.stream.Stream;

public interface ValueExpression extends Tree<ValueExpression> {

    Domain domain();

    // TODO: variables could be implemented with reduce
    Stream<ColumnReference> variables();

    ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer);

    <T> T accept(ValueExpressionVisitor<T> visitor);
}
