package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.algebraizer.formula.SubqueryExtractor;
import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.ast.value.Any;
import fr.awildelephant.rdbms.ast.value.Avg;
import fr.awildelephant.rdbms.ast.value.Count;
import fr.awildelephant.rdbms.ast.value.CountStar;
import fr.awildelephant.rdbms.ast.value.Max;
import fr.awildelephant.rdbms.ast.value.Min;
import fr.awildelephant.rdbms.ast.value.Sum;
import fr.awildelephant.rdbms.execution.aggregation.AnyAggregate;
import fr.awildelephant.rdbms.execution.aggregation.AvgAggregate;
import fr.awildelephant.rdbms.execution.aggregation.CountAggregate;
import fr.awildelephant.rdbms.execution.aggregation.CountStarAggregate;
import fr.awildelephant.rdbms.execution.aggregation.MaxAggregate;
import fr.awildelephant.rdbms.execution.aggregation.MinAggregate;
import fr.awildelephant.rdbms.execution.aggregation.SumAggregate;
import fr.awildelephant.rdbms.schema.ColumnReference;

import static fr.awildelephant.rdbms.algebraizer.AggregationsExtractor.aggregationsExtractor;

public final class ExpressionSplitter {

    private final ColumnNameResolver columnNameResolver;
    private final ColumnReferenceTransformer columnReferenceTransformer;

    public ExpressionSplitter(ColumnNameResolver columnNameResolver, ColumnReferenceTransformer columnReferenceTransformer) {
        this.columnNameResolver = columnNameResolver;
        this.columnReferenceTransformer = columnReferenceTransformer;
    }

    void split(AST expression, SplitExpressionCollector collector) {
        final AggregationsExtractor aggregationsExtractor = aggregationsExtractor(columnNameResolver);

        final AST aggregateFreeOutputColumn = aggregationsExtractor.apply(expression);
        final SubqueryExtractor subqueryExtractor = new SubqueryExtractor();

        collector.addMapAboveAggregates(subqueryExtractor.apply(aggregateFreeOutputColumn));

        for (AST aggregate : aggregationsExtractor.collectedAggregates()) {
            final ColumnReference outputName = columnReferenceTransformer.apply(aggregate);

            if (aggregate instanceof final Any anyAggregate) {
                final AST anyInput = anyAggregate.child();

                if (!(anyInput instanceof ColumnName)) {
                    collector.addMapBelowAggregates(subqueryExtractor.apply(anyInput));
                }

                collector.addAggregate(new AnyAggregate(columnReferenceTransformer.apply(anyInput), outputName));
            } else if (aggregate instanceof final Count countAggregate) {
                final AST countInput = countAggregate.child();

                if (!(countInput instanceof ColumnName)) {
                    collector.addMapBelowAggregates(subqueryExtractor.apply(countInput));
                }

                collector.addAggregate(new CountAggregate(columnReferenceTransformer.apply(countInput),
                        outputName,
                        countAggregate.distinct()));
            } else if (aggregate instanceof CountStar) {
                collector.addAggregate(new CountStarAggregate(outputName));
            } else if (aggregate instanceof Avg) {
                final AST avgInput = ((Avg) aggregate).child();

                if (!(avgInput instanceof ColumnName)) {
                    collector.addMapBelowAggregates(subqueryExtractor.apply(avgInput));
                }

                collector.addAggregate(new AvgAggregate(columnReferenceTransformer.apply(avgInput), outputName));
            } else if (aggregate instanceof Max) {
                final AST maxInput = ((Max) aggregate).child();

                if (!(maxInput instanceof ColumnName)) {
                    collector.addMapBelowAggregates(subqueryExtractor.apply(maxInput));
                }

                collector.addAggregate(new MaxAggregate(columnReferenceTransformer.apply(maxInput), outputName));
            } else if (aggregate instanceof Min) {
                final AST minInput = ((Min) aggregate).child();

                if (!(minInput instanceof ColumnName)) {
                    collector.addMapBelowAggregates(subqueryExtractor.apply(minInput));
                }

                collector.addAggregate(new MinAggregate(columnReferenceTransformer.apply(minInput), outputName));
            } else if (aggregate instanceof Sum) {
                final AST sumInput = ((Sum) aggregate).child();

                if (!(sumInput instanceof ColumnName)) {
                    collector.addMapBelowAggregates(subqueryExtractor.apply(sumInput));
                }

                collector.addAggregate(new SumAggregate(columnReferenceTransformer.apply(sumInput), outputName));
            } else {
                throw new UnsupportedOperationException();
            }
        }

        collector.addAllSubqueries(subqueryExtractor.subqueries());
    }
}
