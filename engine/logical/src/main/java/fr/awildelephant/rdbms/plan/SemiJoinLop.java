package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class SemiJoinLop extends AbstractLop {

    private final LogicalOperator left;
    private final LogicalOperator right;
    private final ValueExpression predicate;
    private final String outputColumnName;

    public SemiJoinLop(LogicalOperator left, LogicalOperator right, ValueExpression predicate, String outputColumnName) {
        super(outputSchema(left, outputColumnName));

        this.left = left;
        this.right = right;
        this.predicate = predicate;
        this.outputColumnName = outputColumnName;
    }

    private static Schema outputSchema(LogicalOperator left, String outputColumnName) {
        return left.schema().extend(List.of(new ColumnMetadata(0, new UnqualifiedColumnReference(outputColumnName), Domain.BOOLEAN, true, false)));
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

    public String ouputColumnName() {
        return outputColumnName;
    }

    @Override
    public LogicalOperator transformInputs(Function<LogicalOperator, LogicalOperator> transformer) {
        return new SemiJoinLop(transformer.apply(left), transformer.apply(right), predicate, outputColumnName);
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
        if (!(obj instanceof SemiJoinLop)) {
            return false;
        }

        final SemiJoinLop other = (SemiJoinLop) obj;

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
                .append("resultColumn", outputColumnName)
                .toString();
    }
}
