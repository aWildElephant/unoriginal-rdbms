package fr.awildelephant.rdbms.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.tree.Tree;

import java.util.function.Function;

public interface ValueExpression extends Tree<ValueExpression> {

    Domain domain();

    ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer);

    <T> T accept(ValueExpressionVisitor<T> visitor);
}
