package fr.awildelephant.rdbms.plan.arithmetic;

public abstract class DefaultValueExpressionVisitor<T> implements ValueExpressionVisitor<T> {

    @Override
    public T visit(AddExpression add) {
        return defaultVisit(add);
    }

    @Override
    public T visit(AndExpression and) {
        return defaultVisit(and);
    }

    @Override
    public T visit(CastExpression cast) {
        return defaultVisit(cast);
    }

    @Override
    public T visit(ConstantExpression constant) {
        return defaultVisit(constant);
    }

    @Override
    public T visit(DivideExpression divide) {
        return defaultVisit(divide);
    }

    @Override
    public T visit(EqualExpression equal) {
        return defaultVisit(equal);
    }

    @Override
    public T visit(GreaterExpression greater) {
        return defaultVisit(greater);
    }

    @Override
    public T visit(GreaterOrEqualExpression greaterOrEqual) {
        return defaultVisit(greaterOrEqual);
    }

    @Override
    public T visit(LessExpression less) {
        return defaultVisit(less);
    }

    @Override
    public T visit(LessOrEqualExpression lessOrEqual) {
        return defaultVisit(lessOrEqual);
    }

    @Override
    public T visit(MultiplyExpression multiply) {
        return defaultVisit(multiply);
    }

    @Override
    public T visit(NotExpression not) {
        return defaultVisit(not);
    }

    @Override
    public T visit(OrExpression or) {
        return defaultVisit(or);
    }

    @Override
    public T visit(SubtractExpression subtract) {
        return defaultVisit(subtract);
    }

    @Override
    public T visit(Variable variable) {
        return defaultVisit(variable);
    }

    protected abstract T defaultVisit(ValueExpression expression);
}
