package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.Schema;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class SubqueryExecutionLop extends AbstractLop {

    private final LogicalOperator input;
    private final LogicalOperator subquery;

    public SubqueryExecutionLop(LogicalOperator input, LogicalOperator subquery) {
        super(createOutputSchema(input.schema(), subquery.schema()));

        this.input = input;
        this.subquery = subquery;
    }

    private static Schema createOutputSchema(Schema input, Schema subquery) {
        return input.extend(subquery.columnNames().stream().map(subquery::column).collect(Collectors.toList()));
    }

    public LogicalOperator input() {
        return input;
    }

    public LogicalOperator subquery() {
        return subquery;
    }

    @Override
    public LogicalOperator transformInputs(Function<LogicalOperator, LogicalOperator> transformer) {
        return new SubqueryExecutionLop(transformer.apply(input), transformer.apply(subquery));
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, subquery);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SubqueryExecutionLop)) {
            return false;
        }

        final SubqueryExecutionLop other = (SubqueryExecutionLop) obj;

        return Objects.equals(input, other.input)
                && Objects.equals(subquery, other.subquery);
    }
}
