package fr.awildelephant.rdbms.execution;

import fr.awildelephant.rdbms.schema.Schema;

public abstract class AbstractBinaryLop extends AbstractLop {

    protected final LogicalOperator left;
    protected final LogicalOperator right;

    protected AbstractBinaryLop(LogicalOperator left, LogicalOperator right, Schema schema) {
        super(schema);
        this.left = left;
        this.right = right;
    }

    public LogicalOperator left() {
        return left;
    }

    public LogicalOperator right() {
        return right;
    }
}
