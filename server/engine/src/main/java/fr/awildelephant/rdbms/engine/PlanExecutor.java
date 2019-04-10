package fr.awildelephant.rdbms.engine;

import fr.awildelephant.rdbms.engine.data.table.ManagedTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.AggregationOperator;
import fr.awildelephant.rdbms.engine.operators.AliasOperator;
import fr.awildelephant.rdbms.engine.operators.BreakdownOperator;
import fr.awildelephant.rdbms.engine.operators.DistinctOperator;
import fr.awildelephant.rdbms.engine.operators.FilterOperator;
import fr.awildelephant.rdbms.engine.operators.MapOperator;
import fr.awildelephant.rdbms.engine.operators.ProjectionOperator;
import fr.awildelephant.rdbms.engine.operators.SortOperator;
import fr.awildelephant.rdbms.engine.operators.TableConstructorOperator;
import fr.awildelephant.rdbms.plan.AggregationLop;
import fr.awildelephant.rdbms.plan.AliasLop;
import fr.awildelephant.rdbms.plan.BaseTableLop;
import fr.awildelephant.rdbms.plan.BreakdownLop;
import fr.awildelephant.rdbms.plan.DistinctLop;
import fr.awildelephant.rdbms.plan.FilterLop;
import fr.awildelephant.rdbms.plan.LopVisitor;
import fr.awildelephant.rdbms.plan.MapLop;
import fr.awildelephant.rdbms.plan.ProjectionLop;
import fr.awildelephant.rdbms.plan.SortLop;
import fr.awildelephant.rdbms.plan.TableConstructorLop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlanExecutor implements LopVisitor<Stream<Table>> {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<String, ManagedTable> tables;

    PlanExecutor(Map<String, ManagedTable> tables) {
        this.tables = tables;
    }

    @Override
    public Stream<Table> visit(AggregationLop aggregationNode) {
        final AggregationOperator operator = new AggregationOperator(aggregationNode.aggregates(),
                                                                     aggregationNode.schema());

        return apply(aggregationNode.input()).map(input -> {
            final UUID operatorId = UUID.randomUUID();
            LOGGER.info("{} - AggregateOperator - inputSize: {}", operatorId, input.numberOfTuples());

            final Table output = operator.compute(input);

            LOGGER.info("{} - AggregateOperator - outputSize: {}}", operatorId, output.numberOfTuples());

            return output;

        });
    }

    @Override
    public Stream<Table> visit(AliasLop aliasNode) {
        final AliasOperator operator = new AliasOperator(aliasNode.schema());

        return apply(aliasNode.input()).map(input -> {
            final UUID operatorId = UUID.randomUUID();
            LOGGER.info("{} - RenameOperator - inputSize: {}", operatorId, input.numberOfTuples());

            final Table output = operator.compute(input);

            LOGGER.info("{} - RenameOperator - outputSize: {}}", operatorId, output.numberOfTuples());

            return output;

        });
    }

    @Override
    public Stream<Table> visit(BaseTableLop baseTable) {
        final ManagedTable table = tables.get(baseTable.name());

        LOGGER.info("BaseTable - {} - size: {}", baseTable.name(), table.numberOfTuples());

        return Stream.of(table);
    }

    @Override
    public Stream<Table> visit(BreakdownLop breakdownNode) {
        final BreakdownOperator operator = new BreakdownOperator(breakdownNode.breakdowns());

        return apply(breakdownNode.input()).flatMap(input -> {
            final UUID operatorId = UUID.randomUUID();
            LOGGER.info("{} - BreakdownOperator - inputSize: {}", operatorId, input.numberOfTuples());

            final List<Table> output = operator.compute(input).collect(Collectors.toList());

            if (output.isEmpty()) {
                throw new UnsupportedOperationException("Grouping on an empty table is not supported");
            }

            final int minSize = output.stream().mapToInt(Table::numberOfTuples).min().getAsInt();
            final int maxSize = output.stream().mapToInt(Table::numberOfTuples).max().getAsInt();

            LOGGER.info("{} - BreakdownOperator - numberOfBuckets: {}, minSize: {}, maxSize {}", operatorId,
                        output.size(), minSize, maxSize);

            return output.stream();
        });
    }

    @Override
    public Stream<Table> visit(DistinctLop distinctNode) {
        final DistinctOperator operator = new DistinctOperator();

        return apply(distinctNode.input()).map(input -> {
            final UUID operatorId = UUID.randomUUID();
            LOGGER.info("{} - DistinctOperator - inputSize: {}", operatorId, input.numberOfTuples());

            final Table output = operator.compute(input);

            LOGGER.info("{} - DistinctOperator - outputSize: {}}", operatorId, output.numberOfTuples());

            return output;

        });
    }

    @Override
    public Stream<Table> visit(FilterLop filter) {
        final FilterOperator operator = new FilterOperator(filter.filter());

        return apply(filter.input()).map(input -> {
            final UUID operatorId = UUID.randomUUID();
            LOGGER.info("{} - FilterOperator - inputSize: {}", operatorId, input.numberOfTuples());

            final Table output = operator.compute(input);

            LOGGER.info("{} - FilterOperator - outputSize: {}}", operatorId, output.numberOfTuples());

            return output;

        });
    }

    @Override
    public Stream<Table> visit(MapLop mapNode) {
        final MapOperator operator = new MapOperator(mapNode.operations(), mapNode.schema());

        return apply(mapNode.input()).map(input -> {
            final UUID operatorId = UUID.randomUUID();
            LOGGER.info("{} - MapOperator - inputSize: {}", operatorId, input.numberOfTuples());

            final Table output = operator.compute(input);

            LOGGER.info("{} - MapOperator - outputSize: {}}", operatorId, output.numberOfTuples());

            return output;

        });
    }

    @Override
    public Stream<Table> visit(ProjectionLop projectionNode) {
        final ProjectionOperator operator = new ProjectionOperator(projectionNode.schema());

        return apply(projectionNode.input()).map(input -> {
            final UUID operatorId = UUID.randomUUID();
            LOGGER.info("{} - ProjectOperator - inputSize: {}", operatorId, input.numberOfTuples());

            final Table output = operator.compute(input);

            LOGGER.info("{} - ProjectOperator - outputSize: {}}", operatorId, output.numberOfTuples());

            return output;

        });
    }

    @Override
    public Stream<Table> visit(SortLop sortNode) {
        final SortOperator operator = new SortOperator(sortNode.schema(), sortNode.columns());

        return apply(sortNode.input()).map(input -> {
            final UUID operatorId = UUID.randomUUID();
            LOGGER.info("{} - SortOperator - inputSize: {}", operatorId, input.numberOfTuples());

            final Table output = operator.compute(input);

            LOGGER.info("{} - SortOperator - outputSize: {}", operatorId, output.numberOfTuples());

            return output;

        });
    }

    @Override
    public Stream<Table> visit(TableConstructorLop tableConstructor) {
        final TableConstructorOperator operator = new TableConstructorOperator(tableConstructor.matrix(),
                                                                               tableConstructor.schema());

        LOGGER.info("TableConstructor");

        return Stream.of(operator.compute(null));
    }
}
