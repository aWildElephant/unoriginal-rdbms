package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.Schema;

import java.util.Objects;

public final class CartesianProductLop extends AbstractLop {

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

    @Override
    public int hashCode() {
        return Objects.hash(leftInput, rightInput);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CartesianProductLop)) {
            return false;
        }

        final CartesianProductLop other = (CartesianProductLop) obj;

        return Objects.equals(leftInput, other.leftInput)
                && Objects.equals(rightInput, other.rightInput);
    }
}
