package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;
import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public final class BetweenExpression implements ValueExpression {

    private final ValueExpression value;
    private final ValueExpression lowerBound;
    private final ValueExpression upperBound;

    private BetweenExpression(ValueExpression value, ValueExpression lowerBound, ValueExpression upperBound) {
        this.value = value;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public static BetweenExpression betweenExpression(ValueExpression value, ValueExpression lowerBound, ValueExpression upperBound) {
        return new BetweenExpression(value, lowerBound, upperBound);
    }

    public ValueExpression value() {
        return value;
    }

    public ValueExpression lowerBound() {
        return lowerBound;
    }

    public ValueExpression upperBound() {
        return upperBound;
    }

    @Override
    public Domain domain() {
        return BOOLEAN;
    }

    @Override
    public Stream<ColumnReference> variables() {
        return Stream.concat(value.variables(), Stream.concat(lowerBound.variables(), upperBound.variables()));
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new BetweenExpression(transformer.apply(value),
                                     transformer.apply(lowerBound),
                                     transformer.apply(upperBound));
    }

    @Override
    public <T> T reduce(Function<ValueExpression, T> function, BinaryOperator<T> accumulator) {
        return accumulator.apply(function.apply(value),
                                 accumulator.apply(function.apply(lowerBound), function.apply(upperBound)));
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, lowerBound, upperBound);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final BetweenExpression other)) {
            return false;
        }

        return Objects.equals(value, other.value)
                && Objects.equals(lowerBound, other.lowerBound)
                && Objects.equals(upperBound, other.upperBound);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("value", value)
                .append("lowerBound", lowerBound)
                .append("upperBound", upperBound)
                .toString();
    }
}
