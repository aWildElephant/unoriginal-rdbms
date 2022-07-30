package fr.awildelephant.rdbms.execution;

import fr.awildelephant.rdbms.schema.Schema;

import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;
import static fr.awildelephant.rdbms.execution.JoinOutputSchemaFactory.innerJoinOutputSchema;

public final class CartesianProductLop extends AbstractLop {

    private final LogicalOperator left;
    private final LogicalOperator right;

    public CartesianProductLop(LogicalOperator left, LogicalOperator right) {
        this(left, right, innerJoinOutputSchema(left.schema(), right.schema()));
    }

    public CartesianProductLop(LogicalOperator left, LogicalOperator right, Schema outputSchema) {
        super(outputSchema);

        this.left = left;
        this.right = right;
    }

    public LogicalOperator left() {
        return left;
    }

    public LogicalOperator right() {
        return right;
    }

    @Override
    public LogicalOperator transformInputs(Function<LogicalOperator, LogicalOperator> transformer) {
        return new CartesianProductLop(transformer.apply(left), transformer.apply(right), schema());
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final CartesianProductLop other)) {
            return false;
        }

        return Objects.equals(left, other.left)
                && Objects.equals(right, other.right);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("left", left)
                .append("right", right)
                .toString();
    }
}
