package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;
import java.util.stream.Stream;

public final class Variable implements ValueExpression {

    private final ColumnReference name;
    private final Domain domain;

    private Variable(ColumnReference name, Domain domain) {
        this.name = name;
        this.domain = domain;
    }

    public static Variable variable(ColumnReference name, Domain domain) {
        return new Variable(name, domain);
    }

    public ColumnReference name() {
        return name;
    }

    @Override
    public Domain domain() {
        return domain;
    }

    @Override
    public Stream<ColumnReference> variables() {
        return Stream.of(name);
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Variable)) {
            return false;
        }

        final Variable other = (Variable) obj;

        return domain == other.domain
                && Objects.equals(name, other.name);
    }
}
