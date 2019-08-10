package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;
import java.util.stream.Stream;

public final class Variable implements ValueExpression {

    private final String name;
    private final Domain domain;

    private Variable(String name, Domain domain) {
        this.name = name;
        this.domain = domain;
    }

    public static Variable variable(String name, Domain domain) {
        return new Variable(name, domain);
    }

    public String name() {
        return name;
    }

    @Override
    public Domain domain() {
        return domain;
    }

    @Override
    public Stream<String> variables() {
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
