package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.schema.Schema;

public class InnerJoinLop extends AbstractLop {

    private final LogicalOperator leftInput;
    private final LogicalOperator rightInput;
    private final Formula joinSpecification;

    public InnerJoinLop(LogicalOperator leftInput, LogicalOperator rightInput, Formula joinSpecification, Schema outputSchema) {
        super(outputSchema);

        this.leftInput = leftInput;
        this.rightInput = rightInput;
        this.joinSpecification = joinSpecification;
    }

    public LogicalOperator leftInput() {
        return leftInput;
    }

    public LogicalOperator rightInput() {
        return rightInput;
    }

    public Formula joinSpecification() {
        return joinSpecification;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
