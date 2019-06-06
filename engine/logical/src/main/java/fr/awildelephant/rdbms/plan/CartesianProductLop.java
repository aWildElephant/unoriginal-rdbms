package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.Schema;

import java.util.stream.Collectors;

public class CartesianProductLop extends AbstractLop {

    private final LogicalOperator leftInput;
    private final LogicalOperator rightInput;

    public CartesianProductLop(LogicalOperator leftInput, LogicalOperator rightInput) {
        super(outputSchema(leftInput.schema(), rightInput.schema()));

        this.leftInput = leftInput;
        this.rightInput = rightInput;
    }

    private static Schema outputSchema(Schema left, Schema right) {
        return left.extend(right.columnNames().stream().map(right::column).collect(Collectors.toList()));
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
