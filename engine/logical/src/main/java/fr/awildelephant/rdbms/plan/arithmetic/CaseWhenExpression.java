package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class CaseWhenExpression implements ValueExpression {

    private final ValueExpression condition;
    private final ValueExpression thenExpression;
    private final ValueExpression elseExpression;
    private final Domain outputDomain;

    private CaseWhenExpression(ValueExpression condition, ValueExpression thenExpression, ValueExpression elseExpression, Domain outputDomain) {
        this.condition = condition;
        this.thenExpression = thenExpression;
        this.elseExpression = elseExpression;
        this.outputDomain = outputDomain;
    }

    public static CaseWhenExpression caseWhenExpression(ValueExpression condition, ValueExpression thenExpression, ValueExpression elseExpression, Domain outputDomain) {
        return new CaseWhenExpression(condition, thenExpression, elseExpression, outputDomain);
    }

    public ValueExpression condition() {
        return condition;
    }

    public ValueExpression thenExpression() {
        return thenExpression;
    }

    public ValueExpression elseExpression() {
        return elseExpression;
    }

    @Override
    public Domain domain() {
        return outputDomain;
    }

    @Override
    public Stream<ColumnReference> variables() {
        return Stream.concat(condition.variables(),
                             Stream.concat(thenExpression.variables(), elseExpression.variables()));
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new CaseWhenExpression(transformer.apply(condition),
                                      transformer.apply(thenExpression),
                                      transformer.apply(elseExpression),
                                      outputDomain);
    }

    @Override
    public <T> T reduce(Function<ValueExpression, T> function, BinaryOperator<T> accumulator) {
        return accumulator.apply(function.apply(condition),
                                 accumulator.apply(function.apply(thenExpression), function.apply(elseExpression)));
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(outputDomain, condition, thenExpression, elseExpression);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final CaseWhenExpression other)) {
            return false;
        }

        return outputDomain == other.outputDomain
                && Objects.equals(condition, other.condition)
                && Objects.equals(thenExpression, other.thenExpression)
                && Objects.equals(elseExpression, other.elseExpression);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("if", condition)
                .append("then", thenExpression)
                .append("else", elseExpression)
                .append("domain", outputDomain)
                .toString();
    }
}
