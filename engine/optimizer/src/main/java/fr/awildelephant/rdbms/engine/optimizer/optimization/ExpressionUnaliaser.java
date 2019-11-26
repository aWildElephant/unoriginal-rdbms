package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.plan.alias.Alias;
import fr.awildelephant.rdbms.plan.arithmetic.AddExpression;
import fr.awildelephant.rdbms.plan.arithmetic.AndExpression;
import fr.awildelephant.rdbms.plan.arithmetic.BetweenExpression;
import fr.awildelephant.rdbms.plan.arithmetic.CaseWhenExpression;
import fr.awildelephant.rdbms.plan.arithmetic.CastExpression;
import fr.awildelephant.rdbms.plan.arithmetic.ConstantExpression;
import fr.awildelephant.rdbms.plan.arithmetic.DivideExpression;
import fr.awildelephant.rdbms.plan.arithmetic.EqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.ExtractYearExpression;
import fr.awildelephant.rdbms.plan.arithmetic.GreaterExpression;
import fr.awildelephant.rdbms.plan.arithmetic.GreaterOrEqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.InExpression;
import fr.awildelephant.rdbms.plan.arithmetic.LessExpression;
import fr.awildelephant.rdbms.plan.arithmetic.LessOrEqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.LikeExpression;
import fr.awildelephant.rdbms.plan.arithmetic.MultiplyExpression;
import fr.awildelephant.rdbms.plan.arithmetic.NotEqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.NotExpression;
import fr.awildelephant.rdbms.plan.arithmetic.OrExpression;
import fr.awildelephant.rdbms.plan.arithmetic.OuterQueryVariable;
import fr.awildelephant.rdbms.plan.arithmetic.SubtractExpression;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpressionVisitor;
import fr.awildelephant.rdbms.plan.arithmetic.Variable;

import java.util.ArrayList;
import java.util.Collection;

import static fr.awildelephant.rdbms.plan.arithmetic.AddExpression.addExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.AndExpression.andExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.BetweenExpression.betweenExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.CaseWhenExpression.caseWhenExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.CastExpression.castExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.DivideExpression.divideExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.EqualExpression.equalExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.ExtractYearExpression.extractYearExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.GreaterExpression.greaterExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.GreaterOrEqualExpression.greaterOrEqualExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.InExpression.inExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.LessExpression.lessExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.LessOrEqualExpression.lessOrEqualExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.LikeExpression.likeExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.MultiplyExpression.multiplyExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.NotEqualExpression.notEqualExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.NotExpression.notExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.OrExpression.orExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.OuterQueryVariable.outerQueryVariable;
import static fr.awildelephant.rdbms.plan.arithmetic.SubtractExpression.subtractExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.Variable.variable;

public final class ExpressionUnaliaser implements ValueExpressionVisitor<ValueExpression> {

    private final Alias alias;

    ExpressionUnaliaser(Alias alias) {
        this.alias = alias;
    }

    @Override
    public ValueExpression visit(AddExpression add) {
        return addExpression(apply(add.left()), apply(add.right()), add.domain());
    }

    @Override
    public ValueExpression visit(AndExpression and) {
        return andExpression(apply(and.left()), apply(and.right()));
    }

    @Override
    public ValueExpression visit(BetweenExpression between) {
        return betweenExpression(apply(between.value()), apply(between.lowerBound()), apply(between.upperBound()));
    }

    @Override
    public ValueExpression visit(CaseWhenExpression caseWhen) {
        return caseWhenExpression(apply(caseWhen.condition()),
                                  apply(caseWhen.thenExpression()),
                                  apply(caseWhen.elseExpression()),
                                  caseWhen.domain());
    }

    @Override
    public ValueExpression visit(CastExpression cast) {
        return castExpression(apply(cast.input()), cast.domain());
    }

    @Override
    public ValueExpression visit(ConstantExpression constant) {
        return constant;
    }

    @Override
    public ValueExpression visit(DivideExpression divide) {
        return divideExpression(apply(divide.left()), apply(divide.right()));
    }

    @Override
    public ValueExpression visit(EqualExpression equal) {
        return equalExpression(apply(equal.left()), apply(equal.right()));
    }

    @Override
    public ValueExpression visit(ExtractYearExpression extractYear) {
        return extractYearExpression(apply(extractYear.input()));
    }

    @Override
    public ValueExpression visit(GreaterExpression greater) {
        return greaterExpression(apply(greater.left()), apply(greater.right()));
    }

    @Override
    public ValueExpression visit(GreaterOrEqualExpression greaterOrEqual) {
        return greaterOrEqualExpression(apply(greaterOrEqual.left()), apply(greaterOrEqual.right()));
    }

    @Override
    public ValueExpression visit(InExpression in) {
        final ValueExpression input = apply(in.input());
        final Collection<ValueExpression> values = new ArrayList<>(in.values().size());
        for (ValueExpression value : in.values()) {
            values.add(apply(value));
        }

        return inExpression(input, values);
    }

    @Override
    public ValueExpression visit(LessExpression less) {
        return lessExpression(apply(less.left()), apply(less.right()));
    }

    @Override
    public ValueExpression visit(LessOrEqualExpression lessOrEqual) {
        return lessOrEqualExpression(apply(lessOrEqual.left()), apply(lessOrEqual.right()));
    }

    @Override
    public ValueExpression visit(LikeExpression like) {
        return likeExpression(apply(like.input()), apply(like.pattern()));
    }

    @Override
    public ValueExpression visit(MultiplyExpression multiply) {
        return multiplyExpression(apply(multiply.left()), apply(multiply.right()), multiply.domain());
    }

    @Override
    public ValueExpression visit(NotExpression not) {
        return notExpression(apply(not.input()));
    }

    @Override
    public ValueExpression visit(NotEqualExpression notEqual) {
        return notEqualExpression(apply(notEqual.left()), apply(notEqual.right()));
    }

    @Override
    public ValueExpression visit(OrExpression or) {
        return orExpression(apply(or.left()), apply(or.right()));
    }

    @Override
    public ValueExpression visit(OuterQueryVariable outerQueryVariable) {
        return outerQueryVariable(alias.unalias(outerQueryVariable.reference()), outerQueryVariable.domain());
    }

    @Override
    public ValueExpression visit(SubtractExpression subtract) {
        return subtractExpression(apply(subtract.left()), apply(subtract.right()), subtract.domain());
    }

    @Override
    public ValueExpression visit(Variable variable) {
        return variable(alias.unalias(variable.name()), variable.domain());
    }
}
