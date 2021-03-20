package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;
import static fr.awildelephant.rdbms.plan.JoinOutputSchemaFactory.semiJoinOutputSchema;

public final class DependentSemiJoinLop extends AbstractBinaryLop {

    private final ValueExpression predicate;
    private final ColumnReference outputColumnName;

    public DependentSemiJoinLop(LogicalOperator left,
                                LogicalOperator right,
                                ColumnReference outputColumnName) {
        this(left, right, null, outputColumnName);
    }

    public DependentSemiJoinLop(LogicalOperator left,
                                LogicalOperator right,
                                ValueExpression predicate,
                                ColumnReference outputColumnName) {
        super(left, right, semiJoinOutputSchema(left.schema(), outputColumnName));
        this.predicate = predicate;
        this.outputColumnName = outputColumnName;
    }

    public ValueExpression predicate() {
        return predicate;
    }

    public ColumnReference outputColumnName() {
        return outputColumnName;
    }

    @Override
    public LogicalOperator transformInputs(Function<LogicalOperator, LogicalOperator> transformer) {
        return new DependentSemiJoinLop(transformer.apply(left), transformer.apply(right), predicate, outputColumnName);
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, predicate, outputColumnName);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DependentSemiJoinLop)) {
            return false;
        }

        final DependentSemiJoinLop other = (DependentSemiJoinLop) obj;

        return Objects.equals(left, other.left)
                && Objects.equals(right, other.right)
                && Objects.equals(predicate, other.predicate)
                && Objects.equals(outputColumnName, other.outputColumnName);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("left", left)
                .append("right", right)
                .append("predicate", predicate)
                .append("outputColumnName", outputColumnName)
                .toString();
    }
}
