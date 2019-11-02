package fr.awildelephant.rdbms.engine;

import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.Reference;
import fr.awildelephant.rdbms.plan.arithmetic.AddExpression;
import fr.awildelephant.rdbms.plan.arithmetic.AndExpression;
import fr.awildelephant.rdbms.plan.arithmetic.BetweenExpression;
import fr.awildelephant.rdbms.plan.arithmetic.CastExpression;
import fr.awildelephant.rdbms.plan.arithmetic.ConstantExpression;
import fr.awildelephant.rdbms.plan.arithmetic.DivideExpression;
import fr.awildelephant.rdbms.plan.arithmetic.EqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.GreaterExpression;
import fr.awildelephant.rdbms.plan.arithmetic.GreaterOrEqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.LessExpression;
import fr.awildelephant.rdbms.plan.arithmetic.LessOrEqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.LikeExpression;
import fr.awildelephant.rdbms.plan.arithmetic.MultiplyExpression;
import fr.awildelephant.rdbms.plan.arithmetic.NotExpression;
import fr.awildelephant.rdbms.plan.arithmetic.OrExpression;
import fr.awildelephant.rdbms.plan.arithmetic.SubtractExpression;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpressionVisitor;
import fr.awildelephant.rdbms.plan.arithmetic.Variable;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.HashMap;
import java.util.Map;

import static fr.awildelephant.rdbms.evaluator.operation.AndOperation.andOperation;
import static fr.awildelephant.rdbms.evaluator.operation.Constant.constant;
import static fr.awildelephant.rdbms.evaluator.operation.DateCastOperation.dateCastOperation;
import static fr.awildelephant.rdbms.evaluator.operation.DateIntervalAddition.dateIntervalAddition;
import static fr.awildelephant.rdbms.evaluator.operation.DateIntervalSubstraction.dateIntervalSubstraction;
import static fr.awildelephant.rdbms.evaluator.operation.DecimalAddition.decimalAddition;
import static fr.awildelephant.rdbms.evaluator.operation.DecimalDivision.decimalDivision;
import static fr.awildelephant.rdbms.evaluator.operation.DecimalMultiplication.decimalMultiplication;
import static fr.awildelephant.rdbms.evaluator.operation.DecimalSubtraction.decimalSubtraction;
import static fr.awildelephant.rdbms.evaluator.operation.IntegerAddition.integerAddition;
import static fr.awildelephant.rdbms.evaluator.operation.IntegerDivision.integerDivision;
import static fr.awildelephant.rdbms.evaluator.operation.IntegerMultiplication.integerMultiplication;
import static fr.awildelephant.rdbms.evaluator.operation.IntegerSubtraction.integerSubtraction;
import static fr.awildelephant.rdbms.evaluator.operation.LikePredicate.likePredicate;
import static fr.awildelephant.rdbms.evaluator.operation.NotOperation.notOperation;
import static fr.awildelephant.rdbms.evaluator.operation.OrOperation.orOperation;
import static fr.awildelephant.rdbms.evaluator.operation.Reference.reference;
import static fr.awildelephant.rdbms.evaluator.operation.comparison.ComparisonFactory.equalComparison;
import static fr.awildelephant.rdbms.evaluator.operation.comparison.ComparisonFactory.lessComparison;
import static fr.awildelephant.rdbms.evaluator.operation.comparison.ComparisonFactory.lessOrEqualComparison;
import static fr.awildelephant.rdbms.schema.Domain.DATE;
import static fr.awildelephant.rdbms.schema.Domain.DECIMAL;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;
import static fr.awildelephant.rdbms.schema.Domain.INTERVAL;
import static fr.awildelephant.rdbms.schema.Domain.TEXT;

public final class ValueExpressionToFormulaTransformer implements ValueExpressionVisitor<Operation> {

    private Map<ColumnReference, Reference> references = new HashMap<>();

    static Formula createFormula(ValueExpression expression) {
        final ValueExpressionToFormulaTransformer transformer = new ValueExpressionToFormulaTransformer();

        return new Formula(transformer.apply(expression), transformer.references);
    }

    @Override
    public Operation visit(AddExpression add) {
        final Operation left = apply(add.left());
        final Operation right = apply(add.right());

        if (left.domain().canBeUsedAs(INTEGER) && right.domain().canBeUsedAs(INTEGER)) {
            return integerAddition(left, right);
        }

        if (left.domain().canBeUsedAs(DECIMAL) && right.domain().canBeUsedAs(DECIMAL)) {
            return decimalAddition(left, right);
        }

        if (left.domain().canBeUsedAs(DATE) && right.domain().canBeUsedAs(INTERVAL)) {
            return dateIntervalAddition(left, right);
        }

        throw new IllegalStateException();
    }

    @Override
    public Operation visit(AndExpression and) {
        return andOperation(apply(and.left()), apply(and.right()));
    }

    @Override
    public Operation visit(BetweenExpression between) {
        final Operation value = apply(between.value());
        final Operation lowerBound = apply(between.lowerBound());
        final Operation upperBound = apply(between.upperBound());

        return andOperation(lessOrEqualComparison(lowerBound, value), lessOrEqualComparison(value, upperBound));
    }

    @Override
    public Operation visit(CastExpression cast) {
        if (cast.domain() != DATE || cast.input().domain() != TEXT) {
            throw new IllegalStateException();
        }

        return dateCastOperation(apply(cast.input()));
    }

    @Override
    public Operation visit(ConstantExpression constantExpression) {
        return constant(constantExpression.value(), constantExpression.domain());
    }

    @Override
    public Operation visit(DivideExpression divide) {
        final Operation left = apply(divide.left());
        final Operation right = apply(divide.right());

        if (left.domain().canBeUsedAs(INTEGER) && right.domain().canBeUsedAs(INTEGER)) {
            return integerDivision(left, right);
        }

        if (left.domain().canBeUsedAs(DECIMAL) && right.domain().canBeUsedAs(DECIMAL)) {
            return decimalDivision(left, right);
        }

        throw new IllegalStateException();
    }

    @Override
    public Operation visit(EqualExpression equal) {
        final Operation left = apply(equal.left());
        final Operation right = apply(equal.right());

        return equalComparison(left, right);
    }

    @Override
    public Operation visit(GreaterExpression greater) {
        final Operation left = apply(greater.left());
        final Operation right = apply(greater.right());

        return lessComparison(right, left);
    }

    @Override
    public Operation visit(GreaterOrEqualExpression greaterOrEqual) {
        final Operation left = apply(greaterOrEqual.left());
        final Operation right = apply(greaterOrEqual.right());

        return lessOrEqualComparison(right, left);
    }

    @Override
    public Operation visit(LessExpression less) {
        final Operation left = apply(less.left());
        final Operation right = apply(less.right());

        return lessComparison(left, right);
    }

    @Override
    public Operation visit(LessOrEqualExpression lessOrEqual) {
        final Operation left = apply(lessOrEqual.left());
        final Operation right = apply(lessOrEqual.right());

        return lessOrEqualComparison(left, right);
    }

    @Override
    public Operation visit(LikeExpression like) {
        final Operation value = apply(like.input());
        final Operation pattern = apply(like.pattern());

        return likePredicate(value, pattern);
    }

    @Override
    public Operation visit(MultiplyExpression multiply) {
        final Operation left = apply(multiply.left());
        final Operation right = apply(multiply.right());

        if (left.domain().canBeUsedAs(INTEGER) && right.domain().canBeUsedAs(INTEGER)) {
            return integerMultiplication(left, right);
        }

        if (left.domain().canBeUsedAs(DECIMAL) && right.domain().canBeUsedAs(DECIMAL)) {
            return decimalMultiplication(left, right);
        }

        throw new IllegalStateException();
    }

    @Override
    public Operation visit(NotExpression not) {
        return notOperation(apply(not.input()));
    }

    @Override
    public Operation visit(OrExpression or) {
        return orOperation(apply(or.left()), apply(or.right()));
    }

    @Override
    public Operation visit(SubtractExpression subtract) {
        final Operation left = apply(subtract.left());
        final Operation right = apply(subtract.right());

        if (left.domain().canBeUsedAs(INTEGER) && right.domain().canBeUsedAs(INTEGER)) {
            return integerSubtraction(left, right);
        }

        if (left.domain().canBeUsedAs(DECIMAL) && right.domain().canBeUsedAs(DECIMAL)) {
            return decimalSubtraction(left, right);
        }

        if (left.domain().canBeUsedAs(Domain.DATE) && right.domain().canBeUsedAs(INTERVAL)) {
            return dateIntervalSubstraction(left, right);
        }

        throw new IllegalStateException();
    }

    @Override
    public Operation visit(Variable variable) {
        return references.computeIfAbsent(variable.name(), unused -> reference(variable.domain()));
    }
}
