package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.Schema;

public class InnerJoinLop extends AbstractLop {

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
}
