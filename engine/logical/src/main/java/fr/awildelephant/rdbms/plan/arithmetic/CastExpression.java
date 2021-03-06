package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class CastExpression implements ValueExpression {

    private final ValueExpression input;
    private final Domain domain;

    private CastExpression(ValueExpression input, Domain domain) {
        this.input = input;
        this.domain = domain;
    }

    public static CastExpression castExpression(ValueExpression input, Domain domain) {
        return new CastExpression(input, domain);
    }

    public ValueExpression input() {
        return input;
    }

    @Override
    public Domain domain() {
        return domain;
    }

    @Override
    public Stream<ColumnReference> variables() {
        return input.variables();
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new CastExpression(transformer.apply(input), domain);
    }

    @Override
    public <T> T reduce(Function<ValueExpression, T> function, BinaryOperator<T> accumulator) {
        return function.apply(input);
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, input);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CastExpression)) {
            return false;
        }

        final CastExpression other = (CastExpression) obj;

        return domain == other.domain
                && Objects.equals(input, other.input);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("input", input)
                .append("castTo", domain)
                .toString();
    }
}
