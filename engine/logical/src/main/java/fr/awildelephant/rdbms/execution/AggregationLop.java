package fr.awildelephant.rdbms.execution;

import fr.awildelephant.rdbms.execution.aggregation.Aggregate;
import fr.awildelephant.rdbms.execution.aggregation.AnyAggregate;
import fr.awildelephant.rdbms.execution.aggregation.AvgAggregate;
import fr.awildelephant.rdbms.execution.aggregation.CountAggregate;
import fr.awildelephant.rdbms.execution.aggregation.CountStarAggregate;
import fr.awildelephant.rdbms.execution.aggregation.MaxAggregate;
import fr.awildelephant.rdbms.execution.aggregation.MinAggregate;
import fr.awildelephant.rdbms.execution.aggregation.SumAggregate;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;
import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;
import static fr.awildelephant.rdbms.schema.Domain.DECIMAL;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;

public final class AggregationLop extends AbstractLop {

    private final LogicalOperator input;
    private final List<ColumnReference> breakdowns;
    private final List<Aggregate> aggregates;

    public AggregationLop(LogicalOperator input, List<ColumnReference> breakdowns, List<Aggregate> aggregates) {
        super(buildOutputSchema(input, breakdowns, aggregates));

        this.input = input;
        this.breakdowns = breakdowns;
        this.aggregates = aggregates;
    }

    private static Schema buildOutputSchema(LogicalOperator input, List<ColumnReference> breakdowns,
                                            List<Aggregate> aggregates) {
        final Schema inputSchema = input.schema();

        final List<ColumnMetadata> outputColumns = new ArrayList<>(breakdowns.size() + aggregates.size());

        for (final ColumnReference breakdown : breakdowns) {
            final Domain domain = inputSchema.column(breakdown).metadata().domain();
            outputColumns.add(new ColumnMetadata(breakdown, domain, false, false));
        }

        for (final Aggregate aggregate : aggregates) {
            final Domain outputType = outputType(inputSchema, aggregate);
            final boolean notNull = !aggregate.outputIsNullable();
            outputColumns.add(new ColumnMetadata(aggregate.outputColumn(), outputType, notNull, false));
        }

        return Schema.of(outputColumns);
    }

    private static Domain outputType(Schema inputSchema, Aggregate aggregate) {
        if (aggregate instanceof AnyAggregate) {
            return BOOLEAN;
        } else if (aggregate instanceof CountAggregate) {
            return INTEGER;
        } else if (aggregate instanceof CountStarAggregate) {
            return INTEGER;
        } else if (aggregate instanceof AvgAggregate) {
            return DECIMAL;
        } else if (aggregate instanceof MaxAggregate maxAggregate) {
            return inputSchema.column(maxAggregate.input()).metadata().domain();
        } else if (aggregate instanceof MinAggregate minAggregate) {
            return inputSchema.column(minAggregate.input()).metadata().domain();
        } else if (aggregate instanceof SumAggregate sumAggregate) {
            return inputSchema.column(sumAggregate.input()).metadata().domain();
        }

        throw new UnsupportedOperationException();
    }

    public LogicalOperator input() {
        return input;
    }

    // TODO: this should be a set
    public List<ColumnReference> breakdowns() {
        return breakdowns;
    }

    public List<Aggregate> aggregates() {
        return aggregates;
    }

    @Override
    public LogicalOperator transformInputs(Function<LogicalOperator, LogicalOperator> transformer) {
        return new AggregationLop(transformer.apply(input), breakdowns, aggregates);
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, breakdowns, aggregates);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final AggregationLop other)) {
            return false;
        }

        return Objects.equals(input, other.input)
                && Objects.equals(breakdowns, other.breakdowns)
                && Objects.equals(aggregates, other.aggregates);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("input", input)
                .append("breakdowns", breakdowns)
                .append("aggregates", aggregates)
                .toString();
    }
}
