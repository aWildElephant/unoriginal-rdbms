package fr.awildelephant.rdbms.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public record CoalesceExpression(List<ValueExpression> arguments) implements ValueExpression {

    // TODO: we could implement COALESCE with no argument and make it return null
    @Override
    public Domain domain() {
        return arguments.getFirst().domain();
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new CoalesceExpression(arguments.stream().map(transformer).toList());
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Collection<? extends ValueExpression> children() {
        return arguments;
    }
}
