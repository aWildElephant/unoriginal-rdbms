package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class IsNullExpression implements ValueExpression {

    private final ValueExpression input;

    public IsNullExpression(ValueExpression input) {
        this.input = input;
    }

    public static IsNullExpression isNullExpression(ValueExpression input) {
        return new IsNullExpression(input);
    }

    public ValueExpression input() {
        return input;
    }

    @Override
    public Domain domain() {
        return Domain.BOOLEAN;
    }

    @Override
    public Stream<ColumnReference> variables() {
        return input.variables();
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return isNullExpression(transformer.apply(input));
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
        return Objects.hashCode(input);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final IsNullExpression other)) {
            return false;
        }

        return Objects.equals(input, other.input);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(input)
                .toString();
    }
}
