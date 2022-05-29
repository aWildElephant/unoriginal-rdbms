package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.function.Function;
import java.util.stream.Stream;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;
import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public final class LikeExpression extends BinaryExpression {

    private LikeExpression(ValueExpression input, ValueExpression pattern) {
        super(input, pattern);
    }

    public static LikeExpression likeExpression(ValueExpression input, ValueExpression pattern) {
        return new LikeExpression(input, pattern);
    }

    public ValueExpression input() {
        return firstChild();
    }

    public ValueExpression pattern() {
        return secondChild();
    }

    @Override
    public Domain domain() {
        return BOOLEAN;
    }

    @Override
    public Stream<ColumnReference> variables() {
        return Stream.concat(firstChild().variables(), secondChild().variables());
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new LikeExpression(transformer.apply(firstChild()), transformer.apply(secondChild()));
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("input", firstChild())
                .append("pattern", secondChild())
                .toString();
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final LikeExpression other)) {
            return false;
        }

        return equalsBinaryNode(other);
    }
}
