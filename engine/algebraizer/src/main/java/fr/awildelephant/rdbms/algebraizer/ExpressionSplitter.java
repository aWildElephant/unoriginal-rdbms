package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.ast.value.Avg;
import fr.awildelephant.rdbms.ast.value.Count;
import fr.awildelephant.rdbms.ast.value.CountStar;
import fr.awildelephant.rdbms.ast.value.Min;
import fr.awildelephant.rdbms.ast.value.Sum;
import fr.awildelephant.rdbms.plan.aggregation.AvgAggregate;
import fr.awildelephant.rdbms.plan.aggregation.CountAggregate;
import fr.awildelephant.rdbms.plan.aggregation.MinAggregate;
import fr.awildelephant.rdbms.plan.aggregation.SumAggregate;

import static fr.awildelephant.rdbms.algebraizer.AggregationsExtractor.aggregationsExtractor;
import static fr.awildelephant.rdbms.plan.aggregation.CountStarAggregate.countStarAggregate;

final class ExpressionSplitter {

    private final ColumnNameResolver columnNameResolver;
    private final ColumnReferenceTransformer columnReferenceTransformer;

    ExpressionSplitter(ColumnNameResolver columnNameResolver, ColumnReferenceTransformer columnReferenceTransformer) {
        this.columnNameResolver = columnNameResolver;
        this.columnReferenceTransformer = columnReferenceTransformer;
    }

    void split(AST expression, SplitExpressionCollector collector) {
        final AggregationsExtractor aggregationsExtractor = aggregationsExtractor(columnNameResolver);

        final AST aggregateFreeOutputColumn = aggregationsExtractor.apply(expression);
        final SubqueryExtractor subqueryExtractor = new SubqueryExtractor();

        collector.addMapAboveAggregates(subqueryExtractor.apply(aggregateFreeOutputColumn));

        for (AST aggregate : aggregationsExtractor.collectedAggregates()) {
            if (aggregate instanceof Count) {
                final Count countAggregate = (Count) aggregate;
                final AST countInput = countAggregate.input();

                if (!(countInput instanceof ColumnName)) {
                    collector.addMapBelowAggregates(subqueryExtractor.apply(countInput));
                }

                collector.addAggregate(new CountAggregate(columnReferenceTransformer.apply(countInput),
                                                          countAggregate.distinct()));
            } else if (aggregate instanceof CountStar) {
                collector.addAggregate(countStarAggregate());
            } else if (aggregate instanceof Avg) {
                final AST avgInput = ((Avg) aggregate).input();

                if (!(avgInput instanceof ColumnName)) {
                    collector.addMapBelowAggregates(subqueryExtractor.apply(avgInput));
                }

                collector.addAggregate(new AvgAggregate(columnReferenceTransformer.apply(avgInput)));
            } else if (aggregate instanceof Min) {
                final AST minInput = ((Min) aggregate).input();

                if (!(minInput instanceof ColumnName)) {
                    collector.addMapBelowAggregates(subqueryExtractor.apply(minInput));
                }

                collector.addAggregate(new MinAggregate(columnReferenceTransformer.apply(minInput)));
            } else if (aggregate instanceof Sum) {
                final AST sumInput = ((Sum) aggregate).input();

                if (!(sumInput instanceof ColumnName)) {
                    collector.addMapBelowAggregates(subqueryExtractor.apply(sumInput));
                }

                collector.addAggregate(new SumAggregate(columnReferenceTransformer.apply(sumInput)));
            } else {
                throw new UnsupportedOperationException();
            }
        }

        collector.addAllSubqueries(subqueryExtractor.subqueries());
    }
}
