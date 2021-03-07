package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;
import static fr.awildelephant.rdbms.plan.JoinOutputSchemaFactory.innerJoinOutputSchema;

public final class InnerJoinLop extends AbstractLop {

    private final LogicalOperator left;
    private final LogicalOperator right;
    private final ValueExpression joinSpecification;

    public InnerJoinLop(LogicalOperator left, LogicalOperator right, ValueExpression joinSpecification) {
        this(left, right, joinSpecification, innerJoinOutputSchema(left.schema(), right.schema()));
    }

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
    public LogicalOperator transformInputs(Function<LogicalOperator, LogicalOperator> transformer) {
        return new InnerJoinLop(transformer.apply(left), transformer.apply(right), joinSpecification, schema());
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

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("left", left)
                .append("right", right)
                .append("predicate", joinSpecification)
                .toString();
    }
}
