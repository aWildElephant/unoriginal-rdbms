package fr.awildelephant.rdbms.engine.optimizer.util;

import fr.awildelephant.rdbms.execution.arithmetic.AddExpression;
import fr.awildelephant.rdbms.execution.arithmetic.AndExpression;
import fr.awildelephant.rdbms.execution.arithmetic.BetweenExpression;
import fr.awildelephant.rdbms.execution.arithmetic.BinaryExpression;
import fr.awildelephant.rdbms.execution.arithmetic.CaseWhenExpression;
import fr.awildelephant.rdbms.execution.arithmetic.CastExpression;
import fr.awildelephant.rdbms.execution.arithmetic.ConstantExpression;
import fr.awildelephant.rdbms.execution.arithmetic.DivideExpression;
import fr.awildelephant.rdbms.execution.arithmetic.EqualExpression;
import fr.awildelephant.rdbms.execution.arithmetic.ExtractYearExpression;
import fr.awildelephant.rdbms.execution.arithmetic.GreaterExpression;
import fr.awildelephant.rdbms.execution.arithmetic.GreaterOrEqualExpression;
import fr.awildelephant.rdbms.execution.arithmetic.InExpression;
import fr.awildelephant.rdbms.execution.arithmetic.IsNullExpression;
import fr.awildelephant.rdbms.execution.arithmetic.LessExpression;
import fr.awildelephant.rdbms.execution.arithmetic.LessOrEqualExpression;
import fr.awildelephant.rdbms.execution.arithmetic.LikeExpression;
import fr.awildelephant.rdbms.execution.arithmetic.MultiplyExpression;
import fr.awildelephant.rdbms.execution.arithmetic.NotEqualExpression;
import fr.awildelephant.rdbms.execution.arithmetic.NotExpression;
import fr.awildelephant.rdbms.execution.arithmetic.OrExpression;
import fr.awildelephant.rdbms.execution.arithmetic.OuterQueryVariable;
import fr.awildelephant.rdbms.execution.arithmetic.SubstringExpression;
import fr.awildelephant.rdbms.execution.arithmetic.SubtractExpression;
import fr.awildelephant.rdbms.execution.arithmetic.ValueExpressionVisitor;
import fr.awildelephant.rdbms.execution.arithmetic.Variable;
import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.stream.Stream;

final class OuterQueryVariableExtractor implements ValueExpressionVisitor<Stream<ColumnReference>> {

    @Override
    public Stream<ColumnReference> visit(OuterQueryVariable outerQueryVariable) {
        return Stream.of(outerQueryVariable.reference());
    }

    @Override
    public Stream<ColumnReference> visit(BetweenExpression between) {
        return StreamHelper.concat(apply(between.value()), apply(between.lowerBound()), apply(between.upperBound()));
    }

    @Override
    public Stream<ColumnReference> visit(CaseWhenExpression caseWhen) {
        return StreamHelper.concat(apply(caseWhen.condition()),
                                   apply(caseWhen.thenExpression()),
                                   apply(caseWhen.elseExpression()));
    }

    @Override
    public Stream<ColumnReference> visit(CastExpression cast) {
        return apply(cast.child());
    }

    @Override
    public Stream<ColumnReference> visit(ConstantExpression constant) {
        return Stream.empty();
    }

    @Override
    public Stream<ColumnReference> visit(ExtractYearExpression extractYear) {
        return apply(extractYear.child());
    }

    @Override
    public Stream<ColumnReference> visit(InExpression in) {
        return Stream.concat(apply(in.input()), in.values().stream().flatMap(this));
    }

    @Override
    public Stream<ColumnReference> visit(LikeExpression like) {
        return Stream.concat(apply(like.input()), apply(like.pattern()));
    }

    @Override
    public Stream<ColumnReference> visit(IsNullExpression isNull) {
        return apply(isNull.child());
    }

    @Override
    public Stream<ColumnReference> visit(NotExpression not) {
        return apply(not.child());
    }

    @Override
    public Stream<ColumnReference> visit(SubstringExpression substring) {
        return StreamHelper.concat(apply(substring.input()), apply(substring.start()), apply(substring.length()));
    }

    @Override
    public Stream<ColumnReference> visit(Variable variable) {
        return Stream.empty();
    }

    @Override
    public Stream<ColumnReference> visit(AddExpression add) {
        return visitBinaryExpression(add);
    }

    @Override
    public Stream<ColumnReference> visit(AndExpression and) {
        return visitBinaryExpression(and);
    }

    @Override
    public Stream<ColumnReference> visit(DivideExpression divide) {
        return visitBinaryExpression(divide);
    }

    @Override
    public Stream<ColumnReference> visit(EqualExpression equal) {
        return visitBinaryExpression(equal);
    }

    @Override
    public Stream<ColumnReference> visit(GreaterExpression greater) {
        return visitBinaryExpression(greater);
    }

    @Override
    public Stream<ColumnReference> visit(GreaterOrEqualExpression greaterOrEqual) {
        return visitBinaryExpression(greaterOrEqual);
    }

    @Override
    public Stream<ColumnReference> visit(SubtractExpression subtract) {
        return visitBinaryExpression(subtract);
    }

    @Override
    public Stream<ColumnReference> visit(LessExpression less) {
        return visitBinaryExpression(less);
    }

    @Override
    public Stream<ColumnReference> visit(LessOrEqualExpression lessOrEqual) {
        return visitBinaryExpression(lessOrEqual);
    }

    @Override
    public Stream<ColumnReference> visit(MultiplyExpression multiply) {
        return visitBinaryExpression(multiply);
    }

    @Override
    public Stream<ColumnReference> visit(NotEqualExpression notEqual) {
        return visitBinaryExpression(notEqual);
    }

    @Override
    public Stream<ColumnReference> visit(OrExpression or) {
        return visitBinaryExpression(or);
    }

    private Stream<ColumnReference> visitBinaryExpression(BinaryExpression binaryExpression) {
        return Stream.concat(apply(binaryExpression.left()), apply(binaryExpression.right()));
    }
}
