package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.arithmetic.AddExpression;
import fr.awildelephant.rdbms.arithmetic.AndExpression;
import fr.awildelephant.rdbms.arithmetic.BetweenExpression;
import fr.awildelephant.rdbms.arithmetic.BinaryExpression;
import fr.awildelephant.rdbms.arithmetic.CaseWhenExpression;
import fr.awildelephant.rdbms.arithmetic.CastExpression;
import fr.awildelephant.rdbms.arithmetic.ConstantExpression;
import fr.awildelephant.rdbms.arithmetic.DivideExpression;
import fr.awildelephant.rdbms.arithmetic.EqualExpression;
import fr.awildelephant.rdbms.arithmetic.ExtractYearExpression;
import fr.awildelephant.rdbms.arithmetic.GreaterExpression;
import fr.awildelephant.rdbms.arithmetic.GreaterOrEqualExpression;
import fr.awildelephant.rdbms.arithmetic.InExpression;
import fr.awildelephant.rdbms.arithmetic.IsNullExpression;
import fr.awildelephant.rdbms.arithmetic.LessExpression;
import fr.awildelephant.rdbms.arithmetic.LessOrEqualExpression;
import fr.awildelephant.rdbms.arithmetic.LikeExpression;
import fr.awildelephant.rdbms.arithmetic.MultiplyExpression;
import fr.awildelephant.rdbms.arithmetic.NotEqualExpression;
import fr.awildelephant.rdbms.arithmetic.NotExpression;
import fr.awildelephant.rdbms.arithmetic.OrExpression;
import fr.awildelephant.rdbms.arithmetic.OuterQueryVariable;
import fr.awildelephant.rdbms.arithmetic.SubstringExpression;
import fr.awildelephant.rdbms.arithmetic.SubtractExpression;
import fr.awildelephant.rdbms.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.arithmetic.ValueExpressionVisitor;
import fr.awildelephant.rdbms.arithmetic.Variable;

public final class ConstantEvaluator implements ValueExpressionVisitor<Boolean> {

    private static final ConstantEvaluator INSTANCE = new ConstantEvaluator();

    private ConstantEvaluator() {

    }

    public static boolean isConstant(ValueExpression expression) {
        return Boolean.TRUE.equals(INSTANCE.apply(expression));
    }

    @Override
    public Boolean visit(BetweenExpression between) {
        return isConstant(between.value()) && isConstant(between.lowerBound()) && isConstant(between.upperBound());
    }

    @Override
    public Boolean visit(CaseWhenExpression caseWhen) {
        return isConstant(caseWhen.condition())
                && isConstant(caseWhen.thenExpression())
                && isConstant(caseWhen.elseExpression());
    }

    @Override
    public Boolean visit(CastExpression cast) {
        return apply(cast.child());
    }

    @Override
    public Boolean visit(ConstantExpression constant) {
        return Boolean.TRUE;
    }

    @Override
    public Boolean visit(ExtractYearExpression extractYear) {
        return apply(extractYear.child());
    }

    @Override
    public Boolean visit(InExpression in) {
        return isConstant(in.input()) && in.values().stream().allMatch(ConstantEvaluator::isConstant);
    }

    @Override
    public Boolean visit(IsNullExpression isNull) {
        return apply(isNull.child());
    }

    @Override
    public Boolean visit(LikeExpression like) {
        return isConstant(like.input()) && isConstant(like.pattern());
    }

    @Override
    public Boolean visit(NotExpression not) {
        return apply(not.child());
    }

    @Override
    public Boolean visit(OuterQueryVariable outerQueryVariable) {
        return Boolean.FALSE;
    }

    @Override
    public Boolean visit(SubstringExpression substring) {
        return isConstant(substring.input()) && isConstant(substring.start()) && isConstant(substring.length());
    }

    @Override
    public Boolean visit(Variable variable) {
        return Boolean.FALSE;
    }

    @Override
    public Boolean visit(AddExpression add) {
        return visitBinaryExpression(add);
    }

    @Override
    public Boolean visit(AndExpression and) {
        return visitBinaryExpression(and);
    }

    @Override
    public Boolean visit(MultiplyExpression multiply) {
        return visitBinaryExpression(multiply);
    }

    @Override
    public Boolean visit(LessExpression less) {
        return visitBinaryExpression(less);
    }

    @Override
    public Boolean visit(LessOrEqualExpression lessOrEqual) {
        return visitBinaryExpression(lessOrEqual);
    }

    @Override
    public Boolean visit(NotEqualExpression notEqual) {
        return visitBinaryExpression(notEqual);
    }

    @Override
    public Boolean visit(OrExpression or) {
        return visitBinaryExpression(or);
    }

    @Override
    public Boolean visit(SubtractExpression subtract) {
        return visitBinaryExpression(subtract);
    }

    @Override
    public Boolean visit(GreaterExpression greater) {
        return visitBinaryExpression(greater);
    }

    @Override
    public Boolean visit(GreaterOrEqualExpression greaterOrEqual) {
        return visitBinaryExpression(greaterOrEqual);
    }

    @Override
    public Boolean visit(DivideExpression divide) {
        return visitBinaryExpression(divide);
    }

    @Override
    public Boolean visit(EqualExpression equal) {
        return visitBinaryExpression(equal);
    }

    private boolean visitBinaryExpression(BinaryExpression expression) {
        return isConstant(expression.left()) && isConstant(expression.right());
    }
}
