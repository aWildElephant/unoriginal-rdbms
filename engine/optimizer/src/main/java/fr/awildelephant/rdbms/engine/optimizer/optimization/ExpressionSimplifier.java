package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.formula.creation.ValueExpressionToFormulaTransformer;
import fr.awildelephant.rdbms.plan.arithmetic.AddExpression;
import fr.awildelephant.rdbms.plan.arithmetic.AndExpression;
import fr.awildelephant.rdbms.plan.arithmetic.BinaryExpression;
import fr.awildelephant.rdbms.plan.arithmetic.ConstantExpression;
import fr.awildelephant.rdbms.plan.arithmetic.DefaultValueExpressionVisitor;
import fr.awildelephant.rdbms.plan.arithmetic.DivideExpression;
import fr.awildelephant.rdbms.plan.arithmetic.EqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.GreaterExpression;
import fr.awildelephant.rdbms.plan.arithmetic.GreaterOrEqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.LessExpression;
import fr.awildelephant.rdbms.plan.arithmetic.LessOrEqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.MultiplyExpression;
import fr.awildelephant.rdbms.plan.arithmetic.NotEqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.OrExpression;
import fr.awildelephant.rdbms.plan.arithmetic.SubtractExpression;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.Schema;

import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.engine.optimizer.optimization.ConstantEvaluator.isConstant;
import static fr.awildelephant.rdbms.plan.arithmetic.ConstantExpression.constantExpression;

/**
 * Simplifies an expression.
 *
 * TODO: we can simplify some more (x * 0, x + 0, x * 1, etc..)
 */
public final class ExpressionSimplifier extends DefaultValueExpressionVisitor<ValueExpression> {

    @Override
    public ValueExpression visit(AddExpression add) {
        return visitBinaryExpression(add);
    }

    @Override
    public ValueExpression visit(AndExpression and) {
        return visitBinaryExpression(and);
    }

    @Override
    public ValueExpression visit(DivideExpression divide) {
        return visitBinaryExpression(divide);
    }

    @Override
    public ValueExpression visit(EqualExpression equal) {
        return visitBinaryExpression(equal);
    }

    @Override
    public ValueExpression visit(GreaterExpression greater) {
        return visitBinaryExpression(greater);
    }

    @Override
    public ValueExpression visit(GreaterOrEqualExpression greaterOrEqual) {
        return visitBinaryExpression(greaterOrEqual);
    }

    @Override
    public ValueExpression visit(LessExpression less) {
        return visitBinaryExpression(less);
    }

    @Override
    public ValueExpression visit(LessOrEqualExpression lessOrEqual) {
        return visitBinaryExpression(lessOrEqual);
    }

    @Override
    public ValueExpression visit(MultiplyExpression multiply) {
        return visitBinaryExpression(multiply);
    }

    @Override
    public ValueExpression visit(NotEqualExpression notEqual) {
        return visitBinaryExpression(notEqual);
    }

    @Override
    public ValueExpression visit(OrExpression or) {
        return visitBinaryExpression(or);
    }

    @Override
    public ValueExpression visit(SubtractExpression subtract) {
        return visitBinaryExpression(subtract);
    }

    private ValueExpression visitBinaryExpression(BinaryExpression expression) {
        if (isNull(expression.left()) || isNull(expression.right())) {
            return constantExpression(nullValue(), expression.domain());
        }

        return simplifyIfConstant(expression);
    }

    // FIXME: if expression is constant and evaluates to null but is not a ConstantExpression, this returns false
    private boolean isNull(ValueExpression expression) {
        return expression instanceof ConstantExpression && ((ConstantExpression) expression).value().isNull();
    }

    @Override
    public ValueExpression visit(ConstantExpression constant) {
        return constant;
    }

    @Override
    protected ValueExpression defaultVisit(ValueExpression expression) {
        return simplifyIfConstant(expression);
    }

    private ValueExpression simplifyIfConstant(ValueExpression expression) {
        if (isConstant(expression)) {
            final DomainValue value = createFormula(expression).evaluate();

            return constantExpression(value, expression.domain());
        } else {
            return expression.transformInputs(this);
        }
    }

    private Operation createFormula(ValueExpression expression) {
        return new ValueExpressionToFormulaTransformer(Schema.EMPTY_SCHEMA).apply(expression);
    }
}
