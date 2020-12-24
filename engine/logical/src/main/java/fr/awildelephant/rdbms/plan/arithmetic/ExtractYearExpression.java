package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;

public final class ExtractYearExpression implements ValueExpression {

    private final ValueExpression input;

    private ExtractYearExpression(ValueExpression input) {
        this.input = input;
    }

    public static ExtractYearExpression extractYearExpression(ValueExpression input) {
        return new ExtractYearExpression(input);
    }

    public ValueExpression input() {
        return input;
    }

    @Override
    public Domain domain() {
        return INTEGER;
    }

    @Override
    public Stream<ColumnReference> variables() {
        return input.variables();
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new ExtractYearExpression(transformer.apply(input));
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
        if (!(obj instanceof ExtractYearExpression)) {
            return false;
        }

        final ExtractYearExpression other = (ExtractYearExpression) obj;

        return Objects.equals(input, other.input);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(input)
                .toString();
    }
}
