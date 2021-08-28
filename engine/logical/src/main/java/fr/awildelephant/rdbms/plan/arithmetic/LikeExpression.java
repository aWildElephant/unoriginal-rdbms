package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;
import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public final class LikeExpression implements ValueExpression {

    private final ValueExpression input;
    private final ValueExpression pattern;

    private LikeExpression(ValueExpression input, ValueExpression pattern) {
        this.input = input;
        this.pattern = pattern;
    }

    public static LikeExpression likeExpression(ValueExpression input, ValueExpression pattern) {
        return new LikeExpression(input, pattern);
    }

    public ValueExpression input() {
        return input;
    }

    public ValueExpression pattern() {
        return pattern;
    }

    @Override
    public Domain domain() {
        return BOOLEAN;
    }

    @Override
    public Stream<ColumnReference> variables() {
        return Stream.concat(input.variables(), pattern.variables());
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new LikeExpression(transformer.apply(input), transformer.apply(pattern));
    }

    @Override
    public <T> T reduce(Function<ValueExpression, T> function, BinaryOperator<T> accumulator) {
        return accumulator.apply(function.apply(input), function.apply(pattern));
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, pattern);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final LikeExpression other)) {
            return false;
        }

        return Objects.equals(input, other.input)
                && Objects.equals(pattern, other.pattern);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("input", input)
                .append("pattern", pattern)
                .toString();
    }
}
