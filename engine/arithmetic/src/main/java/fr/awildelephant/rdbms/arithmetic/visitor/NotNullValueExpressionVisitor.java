package fr.awildelephant.rdbms.arithmetic.visitor;

import fr.awildelephant.rdbms.arithmetic.AddExpression;
import fr.awildelephant.rdbms.arithmetic.AndExpression;
import fr.awildelephant.rdbms.arithmetic.BetweenExpression;
import fr.awildelephant.rdbms.arithmetic.BinaryExpression;
import fr.awildelephant.rdbms.arithmetic.CaseWhenExpression;
import fr.awildelephant.rdbms.arithmetic.CastExpression;
import fr.awildelephant.rdbms.arithmetic.CoalesceExpression;
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
import fr.awildelephant.rdbms.schema.Schema;

import java.util.function.Predicate;

/**
 * Returns true if the expression is sure to never evaluate to UNKNOWN.
 * <p>
 * This visitor might return false for an expression that never evaluate to UNKNOWN.
 */
public class NotNullValueExpressionVisitor implements ValueExpressionVisitor<Boolean>, Predicate<ValueExpression> {

    private final Schema schema;

    public NotNullValueExpressionVisitor(Schema schema) {
        this.schema = schema;
    }

    @Override
    public boolean test(ValueExpression valueExpression) {
        return Boolean.TRUE == apply(valueExpression);
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
    public Boolean visit(BetweenExpression between) {
        return apply(between.value()) && apply(between.lowerBound()) && apply(between.upperBound());
    }

    @Override
    public Boolean visit(CaseWhenExpression caseWhen) {
        return apply(caseWhen.condition()) && apply(caseWhen.thenExpression()) && apply(caseWhen.elseExpression());
    }

    @Override
    public Boolean visit(CastExpression cast) {
        return apply(cast.child());
    }

    @Override
    public Boolean visit(CoalesceExpression coalesce) {
        return coalesce.children().stream().anyMatch(this);
    }

    @Override
    public Boolean visit(ConstantExpression constant) {
        return !constant.value().isNull();
    }

    @Override
    public Boolean visit(DivideExpression divide) {
        return visitBinaryExpression(divide);
    }

    @Override
    public Boolean visit(EqualExpression equal) {
        return visitBinaryExpression(equal);
    }

    @Override
    public Boolean visit(ExtractYearExpression extractYear) {
        return apply(extractYear.child());
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
    public Boolean visit(InExpression in) {
        return apply(in) && in.values().stream().allMatch(this::apply);
    }

    @Override
    public Boolean visit(IsNullExpression isNull) {
        return Boolean.TRUE;
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
    public Boolean visit(LikeExpression like) {
        return apply(like.input()) && apply(like.pattern());
    }

    @Override
    public Boolean visit(MultiplyExpression multiply) {
        return visitBinaryExpression(multiply);
    }

    @Override
    public Boolean visit(NotExpression not) {
        return apply(not.child());
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
    public Boolean visit(OuterQueryVariable outerQueryVariable) {
        return Boolean.FALSE;
    }

    @Override
    public Boolean visit(SubstringExpression substring) {
        return apply(substring.input()) && apply(substring.start()) && apply(substring.length());
    }

    @Override
    public Boolean visit(SubtractExpression subtract) {
        return visitBinaryExpression(subtract);
    }

    @Override
    public Boolean visit(Variable variable) {
        return schema.column(variable.reference()).metadata().notNull();
    }

    private Boolean visitBinaryExpression(BinaryExpression binaryExpression) {
        return apply(binaryExpression.left()) && apply(binaryExpression.right());
    }
}
