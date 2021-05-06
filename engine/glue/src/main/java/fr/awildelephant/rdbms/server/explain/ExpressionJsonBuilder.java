package fr.awildelephant.rdbms.server.explain;

import fr.awildelephant.rdbms.plan.arithmetic.AddExpression;
import fr.awildelephant.rdbms.plan.arithmetic.AndExpression;
import fr.awildelephant.rdbms.plan.arithmetic.BetweenExpression;
import fr.awildelephant.rdbms.plan.arithmetic.BinaryExpression;
import fr.awildelephant.rdbms.plan.arithmetic.CaseWhenExpression;
import fr.awildelephant.rdbms.plan.arithmetic.CastExpression;
import fr.awildelephant.rdbms.plan.arithmetic.ConstantExpression;
import fr.awildelephant.rdbms.plan.arithmetic.DivideExpression;
import fr.awildelephant.rdbms.plan.arithmetic.EqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.ExtractYearExpression;
import fr.awildelephant.rdbms.plan.arithmetic.GreaterExpression;
import fr.awildelephant.rdbms.plan.arithmetic.GreaterOrEqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.InExpression;
import fr.awildelephant.rdbms.plan.arithmetic.IsNullExpression;
import fr.awildelephant.rdbms.plan.arithmetic.LessExpression;
import fr.awildelephant.rdbms.plan.arithmetic.LessOrEqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.LikeExpression;
import fr.awildelephant.rdbms.plan.arithmetic.MultiplyExpression;
import fr.awildelephant.rdbms.plan.arithmetic.NotEqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.NotExpression;
import fr.awildelephant.rdbms.plan.arithmetic.OrExpression;
import fr.awildelephant.rdbms.plan.arithmetic.OuterQueryVariable;
import fr.awildelephant.rdbms.plan.arithmetic.SubstringExpression;
import fr.awildelephant.rdbms.plan.arithmetic.SubtractExpression;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpressionVisitor;
import fr.awildelephant.rdbms.plan.arithmetic.Variable;

public final class ExpressionJsonBuilder implements ValueExpressionVisitor<Void> {

    private final JsonBuilder jsonBuilder;

    public ExpressionJsonBuilder(JsonBuilder jsonBuilder) {
        this.jsonBuilder = jsonBuilder;
    }

    @Override
    public Void visit(AddExpression add) {
        appendBinaryExpression('+', add);

        return null;
    }

    @Override
    public Void visit(AndExpression and) {
        appendBinaryExpression("and", and);

        return null;
    }

    @Override
    public Void visit(DivideExpression divide) {
        appendBinaryExpression('/', divide);

        return null;
    }

    @Override
    public Void visit(EqualExpression equal) {
        appendBinaryExpression('=', equal);

        return null;
    }

    @Override
    public Void visit(GreaterExpression greater) {
        appendBinaryExpression('>', greater);

        return null;
    }

    @Override
    public Void visit(GreaterOrEqualExpression greaterOrEqual) {
        appendBinaryExpression(">=", greaterOrEqual);

        return null;
    }

    @Override
    public Void visit(LessExpression less) {
        appendBinaryExpression('<', less);

        return null;
    }

    @Override
    public Void visit(LessOrEqualExpression lessOrEqual) {
        appendBinaryExpression("<=", lessOrEqual);

        return null;
    }

    @Override
    public Void visit(MultiplyExpression multiply) {
        appendBinaryExpression('*', multiply);

        return null;
    }

    @Override
    public Void visit(NotEqualExpression notEqual) {
        appendBinaryExpression("<>", notEqual);

        return null;
    }

    @Override
    public Void visit(OrExpression or) {
        appendBinaryExpression("or", or);

        return null;
    }

    @Override
    public Void visit(SubtractExpression subtract) {
        appendBinaryExpression('-', subtract);

        return null;
    }

    private void appendBinaryExpression(char symbol, BinaryExpression expression) {
        jsonBuilder.startObject()
                .field("type", symbol);
        jsonBuilder.nextField();
        jsonBuilder.field("left");
        apply(expression.left());
        jsonBuilder.nextField();
        jsonBuilder.field("right");
        apply(expression.right());
        jsonBuilder.endObject();
    }

    private void appendBinaryExpression(String symbol, BinaryExpression expression) {
        jsonBuilder.startObject()
                .field("type", symbol);
        jsonBuilder.nextField();
        jsonBuilder.field("left");
        apply(expression.left());
        jsonBuilder.nextField();
        jsonBuilder.field("right");
        apply(expression.right());
        jsonBuilder.endObject();
    }

    @Override
    public Void visit(BetweenExpression between) {
        jsonBuilder.startObject()
                .field("type", "between");
        jsonBuilder.nextField();
        jsonBuilder.field("input");
        apply(between.value());
        jsonBuilder.nextField();
        jsonBuilder.field("lower_bound");
        apply(between.lowerBound());
        jsonBuilder.nextField();
        jsonBuilder.field("upper_bound");
        apply(between.upperBound());
        jsonBuilder.endObject();

        return null;
    }

    @Override
    public Void visit(CaseWhenExpression caseWhen) {
        jsonBuilder.startObject()
                .field("type", "case_when");

        jsonBuilder.nextField();
                jsonBuilder.field("condition");
        apply(caseWhen.condition());
        jsonBuilder.nextField();
        jsonBuilder.field("then");
        apply(caseWhen.thenExpression());
        jsonBuilder.nextField();
        jsonBuilder.field("else");
        apply(caseWhen.elseExpression());
        jsonBuilder.endObject();

        return null;
    }

    @Override
    public Void visit(CastExpression cast) {
        return null;
    }

    @Override
    public Void visit(ConstantExpression constant) {
        jsonBuilder.startObject()
                .field("type", "constant");
        jsonBuilder.nextField();
        jsonBuilder.field("value", constant.value());
        jsonBuilder.endObject();

        return null;
    }

    @Override
    public Void visit(ExtractYearExpression extractYear) {
        return null;
    }

    @Override
    public Void visit(InExpression in) {
        return null;
    }

    @Override
    public Void visit(IsNullExpression isNull) {
        return null;
    }

    @Override
    public Void visit(LikeExpression like) {
        return null;
    }

    @Override
    public Void visit(NotExpression not) {
        return null;
    }

    @Override
    public Void visit(OuterQueryVariable outerQueryVariable) {
        jsonBuilder.startObject()
                .field("type", "variable")
                .field("name", outerQueryVariable.reference().fullName())
                .field("outer", true);
        jsonBuilder.endObject();

        return null;
    }

    @Override
    public Void visit(SubstringExpression substring) {
        return null;
    }

    @Override
    public Void visit(Variable variable) {
        jsonBuilder.startObject()
                .field("type", "variable")
                .field("name", variable.reference().fullName())
                .field("outer", false);
        jsonBuilder.endObject();

        return null;
    }
}
