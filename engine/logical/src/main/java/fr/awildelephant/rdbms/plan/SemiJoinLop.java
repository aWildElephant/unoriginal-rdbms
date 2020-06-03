package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;

import java.util.Objects;
import java.util.function.Function;

public final class SemiJoinLop extends AbstractLop {

    private final LogicalOperator left;
    private final LogicalOperator right;
    private final ValueExpression predicate;

    public SemiJoinLop(LogicalOperator left, LogicalOperator right, ValueExpression predicate) {
        super(left.schema());

        this.left = left;
        this.right = right;
        this.predicate = predicate;
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
        return new SemiJoinLop(transformer.apply(left), transformer.apply(right), predicate);
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
        if (!(obj instanceof SemiJoinLop)) {
            return false;
        }

        final SemiJoinLop other = (SemiJoinLop) obj;

        return Objects.equals(left, other.left)
                && Objects.equals(right, other.right)
                && Objects.equals(predicate, other.predicate);
    }
}
