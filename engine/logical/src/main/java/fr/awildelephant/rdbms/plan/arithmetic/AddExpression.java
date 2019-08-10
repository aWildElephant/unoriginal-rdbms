package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;

public final class AddExpression extends BinaryExpression {

    private final Domain domain;

    private AddExpression(ValueExpression left, ValueExpression right, Domain domain) {
        super(left, right);
        this.domain = domain;
    }

    public static AddExpression addExpression(ValueExpression left, ValueExpression right, Domain domain) {
        return new AddExpression(left, right, domain);
    }

    public Domain domain() {
        return domain;
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, left, right);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AddExpression)) {
            return false;
        }

        final AddExpression other = (AddExpression) obj;

        return domain == other.domain
                && Objects.equals(left, other.left)
                && Objects.equals(right, other.right);
    }
}
