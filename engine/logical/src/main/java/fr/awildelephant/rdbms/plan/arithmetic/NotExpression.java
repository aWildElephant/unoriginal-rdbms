package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;
import java.util.stream.Stream;

import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public final class NotExpression implements ValueExpression {

    private final ValueExpression input;

    private NotExpression(ValueExpression input) {
        this.input = input;
    }

    public static NotExpression notExpression(ValueExpression input) {
        return new NotExpression(input);
    }

    public ValueExpression input() {
        return input;
    }

    @Override
    public Domain domain() {
        return BOOLEAN;
    }

    @Override
    public Stream<String> variables() {
        return input.variables();
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
        if (!(obj instanceof NotExpression)) {
            return false;
        }

        final NotExpression other = (NotExpression) obj;

        return Objects.equals(input, other.input);
    }
}
