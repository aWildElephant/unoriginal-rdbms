package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;
import static fr.awildelephant.rdbms.schema.Domain.TEXT;

public final class SubstringExpression implements ValueExpression {

    private final ValueExpression input;
    private final ValueExpression start;
    private final ValueExpression length;

    private SubstringExpression(ValueExpression input, ValueExpression start, ValueExpression length) {
        this.input = input;
        this.start = start;
        this.length = length;
    }

    public static SubstringExpression substringExpression(ValueExpression input, ValueExpression start, ValueExpression length) {
        return new SubstringExpression(input, start, length);
    }

    public ValueExpression input() {
        return input;
    }

    public ValueExpression start() {
        return start;
    }

    public ValueExpression length() {
        return length;
    }

    @Override
    public Domain domain() {
        return TEXT;
    }

    @Override
    public Stream<ColumnReference> variables() {
        return Stream.concat(input.variables(),
                             Stream.concat(start.variables(), length.variables()));
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new SubstringExpression(transformer.apply(input),
                                       transformer.apply(start),
                                       transformer.apply(length));
    }

    @Override
    public <T> T reduce(Function<ValueExpression, T> function, BinaryOperator<T> accumulator) {
        return accumulator.apply(function.apply(input),
                                 accumulator.apply(function.apply(start), function.apply(length)));
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, start, length);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SubstringExpression)) {
            return false;
        }

        final SubstringExpression other = (SubstringExpression) obj;

        return Objects.equals(input, other.input)
                && Objects.equals(start, other.start)
                && Objects.equals(length, other.length);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("input", input)
                .append("start", start)
                .append("length", length)
                .toString();
    }
}
