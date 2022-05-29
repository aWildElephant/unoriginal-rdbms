package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.tree.TernaryNode;

import java.util.function.Function;
import java.util.stream.Stream;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;
import static fr.awildelephant.rdbms.schema.Domain.TEXT;

public final class SubstringExpression extends TernaryNode<ValueExpression, ValueExpression, ValueExpression, ValueExpression>
        implements ValueExpression {

    private SubstringExpression(ValueExpression input, ValueExpression start, ValueExpression length) {
        super(input, start, length);
    }

    public static SubstringExpression substringExpression(ValueExpression input, ValueExpression start, ValueExpression length) {
        return new SubstringExpression(input, start, length);
    }

    public ValueExpression input() {
        return firstChild();
    }

    public ValueExpression start() {
        return secondChild();
    }

    public ValueExpression length() {
        return thirdChild();
    }

    @Override
    public Domain domain() {
        return TEXT;
    }

    @Override
    public Stream<ColumnReference> variables() {
        return Stream.concat(firstChild().variables(),
                Stream.concat(secondChild().variables(), thirdChild().variables()));
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new SubstringExpression(transformer.apply(firstChild()),
                transformer.apply(secondChild()),
                transformer.apply(thirdChild()));
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("input", firstChild())
                .append("start", secondChild())
                .append("length", thirdChild())
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final SubstringExpression other)) {
            return false;
        }

        return equalsTernary(other);
    }
}
