package fr.awildelephant.rdbms.plan.arithmetic;

import java.util.function.Function;

public interface ValueExpressionVisitor<T> extends Function<ValueExpression, T> {

    default T apply(ValueExpression expression) {
        return expression.accept(this);
    }

    T visit(AddExpression add);

    T visit(AndExpression and);

    T visit(BetweenExpression between);

    T visit(CastExpression cast);

    T visit(ConstantExpression constant);

    T visit(DivideExpression divide);

    T visit(EqualExpression equal);

    T visit(GreaterExpression greater);

    T visit(GreaterOrEqualExpression greaterOrEqual);

    T visit(LessExpression less);

    T visit(LessOrEqualExpression lessOrEqual);

    T visit(LikeExpression like);

    T visit(MultiplyExpression multiply);

    T visit(NotExpression not);

    T visit(OrExpression or);

    T visit(SubtractExpression subtract);

    T visit(Variable variable);
}
