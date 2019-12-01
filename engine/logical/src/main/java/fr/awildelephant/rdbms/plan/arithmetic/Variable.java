package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public final class Variable implements ValueExpression {

    private final ColumnReference reference;
    private final Domain domain;

    private Variable(ColumnReference reference, Domain domain) {
        this.reference = reference;
        this.domain = domain;
    }

    public static Variable variable(ColumnReference reference, Domain domain) {
        return new Variable(reference, domain);
    }

    public ColumnReference reference() {
        return reference;
    }

    @Override
    public Domain domain() {
        return domain;
    }

    @Override
    public Stream<ColumnReference> variables() {
        return Stream.of(reference);
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return this;
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, reference);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Variable)) {
            return false;
        }

        final Variable other = (Variable) obj;

        return domain == other.domain
                && Objects.equals(reference, other.reference);
    }
}
