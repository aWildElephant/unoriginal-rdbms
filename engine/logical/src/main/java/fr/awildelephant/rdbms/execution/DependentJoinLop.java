package fr.awildelephant.rdbms.execution;

import fr.awildelephant.rdbms.execution.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;
import static fr.awildelephant.rdbms.execution.JoinOutputSchemaFactory.innerJoinOutputSchema;

public final class DependentJoinLop extends AbstractLop {

    private final LogicalOperator left;
    private final LogicalOperator right;
    private final ValueExpression predicate;

    public DependentJoinLop(LogicalOperator left, LogicalOperator right) {
        this(left, right, null);
    }

    public DependentJoinLop(LogicalOperator left, LogicalOperator right, ValueExpression predicate) {
        super(createOutputSchema(left.schema(), right.schema()));

        this.left = left;
        this.right = right;
        this.predicate = predicate;
    }

    private static Schema createOutputSchema(Schema input, Schema subquery) {
        return innerJoinOutputSchema(input, subquery);
    }

    public LogicalOperator left() {
        return left;
    }

    public LogicalOperator right() {
        return right;
    }

    public ValueExpression predicate() {
        return predicate;
    }

    @Override
    public LogicalOperator transformInputs(Function<LogicalOperator, LogicalOperator> transformer) {
        return new DependentJoinLop(transformer.apply(left), transformer.apply(right), predicate);
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, predicate);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final DependentJoinLop other)) {
            return false;
        }

        return Objects.equals(left, other.left)
                && Objects.equals(right, other.right)
                && Objects.equals(predicate, other.predicate);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("left", left)
                .append("right", right)
                .append("predicate", predicate)
                .toString();
    }
}
