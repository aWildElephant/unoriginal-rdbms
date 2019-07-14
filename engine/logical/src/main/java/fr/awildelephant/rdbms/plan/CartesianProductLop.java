package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.Schema;

public class CartesianProductLop extends AbstractLop {

    private final LogicalOperator leftInput;
    private final LogicalOperator rightInput;

    public CartesianProductLop(LogicalOperator leftInput, LogicalOperator rightInput, Schema outputSchema) {
        super(outputSchema);

        this.leftInput = leftInput;
        this.rightInput = rightInput;
    }

    public LogicalOperator leftInput() {
        return leftInput;
    }

    public LogicalOperator rightInput() {
        return rightInput;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
