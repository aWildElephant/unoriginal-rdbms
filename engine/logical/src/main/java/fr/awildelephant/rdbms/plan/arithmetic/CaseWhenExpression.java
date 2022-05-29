package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.tree.TernaryNode;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class CaseWhenExpression extends TernaryNode<ValueExpression, ValueExpression, ValueExpression, ValueExpression>
        implements ValueExpression {

    private final Domain outputDomain;

    private CaseWhenExpression(ValueExpression condition, ValueExpression thenExpression, ValueExpression elseExpression, Domain outputDomain) {
        super(condition, thenExpression, elseExpression);
        this.outputDomain = outputDomain;
    }

    public static CaseWhenExpression caseWhenExpression(ValueExpression condition, ValueExpression thenExpression, ValueExpression elseExpression, Domain outputDomain) {
        return new CaseWhenExpression(condition, thenExpression, elseExpression, outputDomain);
    }

    public ValueExpression condition() {
        return firstChild();
    }

    public ValueExpression thenExpression() {
        return secondChild();
    }

    public ValueExpression elseExpression() {
        return thirdChild();
    }

    @Override
    public Domain domain() {
        return outputDomain;
    }

    @Override
    public Stream<ColumnReference> variables() {
        return Stream.concat(firstChild().variables(),
                Stream.concat(secondChild().variables(), thirdChild().variables()));
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new CaseWhenExpression(transformer.apply(firstChild()),
                transformer.apply(secondChild()),
                transformer.apply(thirdChild()),
                outputDomain);
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("if", firstChild())
                .append("then", secondChild())
                .append("else", thirdChild())
                .append("domain", outputDomain)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(outputDomain, firstChild(), secondChild(), thirdChild());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final CaseWhenExpression other)) {
            return false;
        }

        return outputDomain == other.outputDomain
                && equalsTernary(other);
    }
}
