package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.plan.aggregation.Aggregate;
import fr.awildelephant.rdbms.plan.aggregation.AvgAggregate;
import fr.awildelephant.rdbms.plan.aggregation.CountAggregate;
import fr.awildelephant.rdbms.plan.aggregation.CountStarAggregate;
import fr.awildelephant.rdbms.plan.aggregation.MinAggregate;
import fr.awildelephant.rdbms.plan.aggregation.SumAggregate;
import fr.awildelephant.rdbms.schema.Column;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.schema.Domain.DECIMAL;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;

public class AggregationLop extends AbstractLop {

    private final List<Aggregate> aggregates;
    private final LogicalOperator input;

    public AggregationLop(List<Aggregate> aggregates, LogicalOperator input) {
        super(buildOutputSchema(input, aggregates));

        this.aggregates = aggregates;
        this.input = input;
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
        } else if (aggregate instanceof MinAggregate) {
            return inputSchema.column(((MinAggregate) aggregate).input()).domain();
        } else if (aggregate instanceof SumAggregate) {
            return inputSchema.column(((SumAggregate) aggregate).input()).domain();
        }

        throw new UnsupportedOperationException();
    }

    public List<Aggregate> aggregates() {
        return aggregates;
    }

    public LogicalOperator input() {
        return input;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
