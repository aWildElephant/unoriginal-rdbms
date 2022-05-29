package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;
import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public final class InExpression implements ValueExpression {

    private final ValueExpression input;
    private final Collection<ValueExpression> values;

    private InExpression(ValueExpression input, Collection<ValueExpression> values) {
        this.input = input;
        this.values = values;
    }

    public static InExpression inExpression(ValueExpression input, Collection<ValueExpression> values) {
        return new InExpression(input, values);
    }

    public ValueExpression input() {
        return input;
    }

    public Collection<ValueExpression> values() {
        return values;
    }

    @Override
    public Domain domain() {
        return BOOLEAN;
    }

    @Override
    public Stream<ColumnReference> variables() {
        return Stream.concat(input.variables(), values.stream().flatMap(ValueExpression::variables));
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        final Collection<ValueExpression> transformedValues = new ArrayList<>(values.size());
        for (ValueExpression value : values) {
            transformedValues.add(transformer.apply(value));
        }

        return new InExpression(transformer.apply(input), transformedValues);
    }

    @Override
    public <T> T reduce(Function<ValueExpression, T> function, BinaryOperator<T> accumulator) {
        final T inputResult = function.apply(input);

        final Optional<T> valuesResult = values.stream().map(function).reduce(accumulator);

        if (valuesResult.isPresent()) {
            return accumulator.apply(inputResult, valuesResult.get());
        }

        return inputResult;
    }

    @Override
    public Collection<? extends ValueExpression> children() {
        final List<ValueExpression> children = new ArrayList<>();
        children.add(input);
        children.addAll(values);
        return children;
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("input", input)
                .append("values", values)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, values);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final InExpression other)) {
            return false;
        }

        return Objects.equals(input, other.input)
                && Objects.equals(values, other.values);
    }
}
