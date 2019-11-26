package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

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
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, values);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ValueExpression)) {
            return false;
        }

        final InExpression other = (InExpression) obj;

        return Objects.equals(input, other.input)
                && Objects.equals(values, other.values);
    }
}
