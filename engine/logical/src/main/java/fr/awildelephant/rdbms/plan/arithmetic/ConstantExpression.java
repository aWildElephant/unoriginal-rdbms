package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;
import java.util.stream.Stream;

public final class ConstantExpression implements ValueExpression {

    private final DomainValue value;
    private final Domain domain;

    private ConstantExpression(DomainValue value, Domain domain) {
        this.value = value;
        this.domain = domain;
    }

    public static ConstantExpression constantExpression(DomainValue value, Domain domain) {
        return new ConstantExpression(value, domain);
    }

    public DomainValue value() {
        return value;
    }

    @Override
    public Domain domain() {
        return domain;
    }

    @Override
    public Stream<ColumnReference> variables() {
        return Stream.of();
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ConstantExpression)) {
            return false;
        }

        final ConstantExpression other = (ConstantExpression) obj;

        return domain == other.domain
                && Objects.equals(value, other.value);
    }
}
