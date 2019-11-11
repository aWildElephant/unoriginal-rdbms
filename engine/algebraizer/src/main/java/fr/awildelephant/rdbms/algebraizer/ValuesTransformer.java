package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.ast.value.Avg;
import fr.awildelephant.rdbms.ast.value.CountStar;
import fr.awildelephant.rdbms.ast.value.Min;
import fr.awildelephant.rdbms.ast.value.Sum;
import fr.awildelephant.rdbms.plan.AggregationLop;
import fr.awildelephant.rdbms.plan.CollectLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.MapLop;
import fr.awildelephant.rdbms.plan.aggregation.Aggregate;
import fr.awildelephant.rdbms.plan.aggregation.AvgAggregate;
import fr.awildelephant.rdbms.plan.aggregation.CountStarAggregate;
import fr.awildelephant.rdbms.plan.aggregation.MinAggregate;
import fr.awildelephant.rdbms.plan.aggregation.SumAggregate;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.algebraizer.ASTToValueExpressionTransformer.createValueExpression;
import static fr.awildelephant.rdbms.algebraizer.AggregationsExtractor.aggregationsExtractor;
import static fr.awildelephant.rdbms.algebraizer.FormulaOrNotFormulaDifferentiator.isFormula;

final class ValuesTransformer {

    private final ColumnNameResolver columnNameResolver;
    private final ColumnReferenceTransformer columnReferenceTransformer;

    ValuesTransformer(ColumnNameResolver columnNameResolver, ColumnReferenceTransformer columnReferenceTransformer) {
        this.columnNameResolver = columnNameResolver;
        this.columnReferenceTransformer = columnReferenceTransformer;
    }

    LogicalOperator transform(LogicalOperator input, List<AST> values) {
        final AggregationsExtractor aggregationsExtractor = aggregationsExtractor(columnNameResolver);

        final ArrayList<AST> mapsBelowAggregates = new ArrayList<>();
        final List<Aggregate> aggregates = new ArrayList<>();
        final List<AST> mapsOverAggregates = new ArrayList<>();

        for (AST column : values) {
            final AST aggregateFreeOutputColumn = aggregationsExtractor.apply(column);

            if (isFormula(aggregateFreeOutputColumn)) {
                mapsOverAggregates.add(aggregateFreeOutputColumn);
            }
        }

        for (AST aggregate : aggregationsExtractor.collectedAggregates()) {
            if (aggregate instanceof CountStar) {
                aggregates.add(new CountStarAggregate());
            } else if (aggregate instanceof Avg) {
                final AST avgInput = ((Avg) aggregate).input();

                if (!(avgInput instanceof ColumnName)) {
                    mapsBelowAggregates.add(avgInput);
                }

                aggregates.add(new AvgAggregate(columnReferenceTransformer.apply(avgInput)));
            } else if (aggregate instanceof Min) {
                final AST minInput = ((Min) aggregate).input();

                if (!(minInput instanceof ColumnName)) {
                    mapsBelowAggregates.add(minInput);
                }

                aggregates.add(new MinAggregate(columnReferenceTransformer.apply(minInput)));
            } else if (aggregate instanceof Sum) {
                final AST sumInput = ((Sum) aggregate).input();

                if (!(sumInput instanceof ColumnName)) {
                    mapsBelowAggregates.add(sumInput);
                }

                aggregates.add(new SumAggregate(columnReferenceTransformer.apply(sumInput)));
            } else {
                throw new UnsupportedOperationException();
            }
        }

        LogicalOperator lastOperator = input;

        if (!mapsBelowAggregates.isEmpty()) {
            lastOperator = createMapOperator(mapsBelowAggregates, lastOperator);
        }

        if (!aggregates.isEmpty()) {
            lastOperator = new AggregationLop(aggregates, lastOperator);
        }

        if (!mapsOverAggregates.isEmpty()) {
            lastOperator = createMapOperator(mapsOverAggregates, lastOperator);
        }

        if (!aggregates.isEmpty()) {
            // Fixme: we need a collect operator only if we grouped. Ideally, the logical plan should not include this node at all.
            lastOperator = new CollectLop(lastOperator);
        }

        return lastOperator;
    }

    private LogicalOperator createMapOperator(List<AST> expressions, LogicalOperator input) {
        final List<ValueExpression> valueExpressions = createValueExpressions(input, expressions);

        final List<String> outputNames = getOutputNames(expressions);

        return new MapLop(input, valueExpressions, outputNames);
    }

    private List<String> getOutputNames(List<AST> expressions) {
        final List<String> outputNames = new ArrayList<>(expressions.size());

        for (AST expression : expressions) {
            outputNames.add(columnNameResolver.apply(expression));
        }

        return outputNames;
    }

    private List<ValueExpression> createValueExpressions(LogicalOperator input, List<AST> expressions) {
        final List<ValueExpression> valueExpressions = new ArrayList<>(expressions.size());

        for (AST expression : expressions) {
            valueExpressions.add(createValueExpression(expression, input.schema()));
        }

        return valueExpressions;
    }
}
