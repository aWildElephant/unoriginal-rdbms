package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.Objects;

public final class InnerJoinLop extends AbstractLop {

    private final LogicalOperator left;
    private final LogicalOperator right;
    private final ValueExpression joinSpecification;

    public InnerJoinLop(LogicalOperator left, LogicalOperator right, ValueExpression joinSpecification, Schema outputSchema) {
        super(outputSchema);

        this.left = left;
        this.right = right;
        this.joinSpecification = joinSpecification;
    }

    public LogicalOperator left() {
        return left;
    }

    public LogicalOperator right() {
        return right;
    }

    public ValueExpression joinSpecification() {
        return joinSpecification;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, joinSpecification);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InnerJoinLop)) {
            return false;
        }

        final InnerJoinLop other = (InnerJoinLop) obj;

        return Objects.equals(left, other.left)
                && Objects.equals(right, other.right)
                && Objects.equals(joinSpecification, other.joinSpecification);
    }
}
