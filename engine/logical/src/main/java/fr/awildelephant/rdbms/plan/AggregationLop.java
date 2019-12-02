package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.plan.aggregation.Aggregate;
import fr.awildelephant.rdbms.plan.aggregation.AvgAggregate;
import fr.awildelephant.rdbms.plan.aggregation.CountAggregate;
import fr.awildelephant.rdbms.plan.aggregation.CountStarAggregate;
import fr.awildelephant.rdbms.plan.aggregation.MaxAggregate;
import fr.awildelephant.rdbms.plan.aggregation.MinAggregate;
import fr.awildelephant.rdbms.plan.aggregation.SumAggregate;
import fr.awildelephant.rdbms.schema.Column;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.schema.Domain.DECIMAL;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;

public final class AggregationLop extends AbstractLop {

    private final LogicalOperator input;
    private final List<Aggregate> aggregates;

    public AggregationLop(LogicalOperator input, List<Aggregate> aggregates) {
        super(buildOutputSchema(input, aggregates));

        this.input = input;
        this.aggregates = aggregates;
    }

    private static Schema buildOutputSchema(LogicalOperator input, List<Aggregate> aggregates) {
        final Schema inputSchema = input.schema();

        final int firstIndex = inputSchema.numberOfAttributes();

        final List<Column> aggregateColumns = new ArrayList<>(aggregates.size());
        for (int i = 0; i < aggregates.size(); i++) {
            final Aggregate aggregate = aggregates.get(i);

            final Domain outputType = outputType(inputSchema, aggregate);

            aggregateColumns.add(new Column(firstIndex + i, aggregate.outputName(), outputType,
                                            !aggregate.outputIsNullable(), false));
        }

        return inputSchema.extend(aggregateColumns);
    }

    private static Domain outputType(Schema inputSchema, Aggregate aggregate) {
        if (aggregate instanceof CountAggregate) {
            return INTEGER;
        } else if (aggregate instanceof CountStarAggregate) {
            return INTEGER;
        } else if (aggregate instanceof AvgAggregate) {
            return DECIMAL;
        } else if (aggregate instanceof MaxAggregate) {
            return inputSchema.column(((MaxAggregate) aggregate).input()).domain();
        } else if (aggregate instanceof MinAggregate) {
            return inputSchema.column(((MinAggregate) aggregate).input()).domain();
        } else if (aggregate instanceof SumAggregate) {
            return inputSchema.column(((SumAggregate) aggregate).input()).domain();
        }

        throw new UnsupportedOperationException();
    }

    public LogicalOperator input() {
        return input;
    }

    public List<Aggregate> aggregates() {
        return aggregates;
    }

    @Override
    public LogicalOperator transformInputs(Function<LogicalOperator, LogicalOperator> transformer) {
        return new AggregationLop(transformer.apply(input), aggregates);
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, aggregates);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AggregationLop)) {
            return false;
        }

        final AggregationLop other = (AggregationLop) obj;

        return Objects.equals(input, other.input)
                && Objects.equals(aggregates, other.aggregates);
    }
}
