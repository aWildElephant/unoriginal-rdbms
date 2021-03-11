package fr.awildelephant.rdbms.engine.optimizer.optimization.unnesting;

import fr.awildelephant.rdbms.plan.AliasLop;
import fr.awildelephant.rdbms.plan.DependentJoinLop;
import fr.awildelephant.rdbms.plan.DistinctLop;
import fr.awildelephant.rdbms.plan.InnerJoinLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.ProjectionLop;
import fr.awildelephant.rdbms.plan.alias.Alias;
import fr.awildelephant.rdbms.plan.alias.ColumnAlias;
import fr.awildelephant.rdbms.plan.alias.ColumnAliasBuilder;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static fr.awildelephant.rdbms.engine.optimizer.optimization.util.OptimizationHelper.alwaysTrue;
import static fr.awildelephant.rdbms.engine.optimizer.util.AttributesFunction.attributes;
import static fr.awildelephant.rdbms.engine.optimizer.util.FreeVariablesFunction.freeVariables;
import static fr.awildelephant.rdbms.engine.optimizer.util.SetHelper.intersection;
import static fr.awildelephant.rdbms.plan.JoinOutputSchemaFactory.innerJoinOutputSchema;
import static fr.awildelephant.rdbms.plan.arithmetic.AndExpression.andExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.EqualExpression.equalExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.FilterCollapser.collapseFilters;
import static fr.awildelephant.rdbms.plan.arithmetic.Variable.variable;

/**
 * Decorrelate subqueries using the method described in "Unnesting Arbitrary Queries" by Thomas Neumann and Alfons Kemper.
 * <p>
 * NeumannKemperSubqueryDecorrelator sounds cool, but the paper doesn't say the method should be named that way.
 */
public final class NeumannKemperSubqueryDecorrelator {

    public LogicalOperator decorrelate(DependentJoinLop plan) {
        final LogicalOperator t1 = plan.left();
        final LogicalOperator t2 = plan.right();

        final Set<ColumnReference> correlatedVariables = intersection(freeVariables(t2), attributes(t1));
        if (correlatedVariables.isEmpty()) {
            return new InnerJoinLop(t1, t2, plan.predicate());
        }

        final LogicalOperator rewrittenJoin = rewriteDependentJoin(t1, t2, plan.predicate(), correlatedVariables);

        return new DependentJoinPushDown().apply(rewrittenJoin);
    }

    /**
     * This is similar to a magic set rewriting.
     */
    private LogicalOperator rewriteDependentJoin(LogicalOperator t1,
                                                 LogicalOperator t2,
                                                 ValueExpression predicate,
                                                 Collection<ColumnReference> correlatedColumns) {
        final DistinctLop magicSet = magicSet(t1, new ArrayList<>(correlatedColumns));

        final DependentJoinLop pushedDownDependentJoin = new DependentJoinLop(magicSet, t2, alwaysTrue());

        final ColumnAliasBuilder columnAliasBuilder = new ColumnAliasBuilder();
        for (ColumnReference correlatedColumn : correlatedColumns) {
            columnAliasBuilder.add(correlatedColumn, UUID.randomUUID().toString());
        }

        final ColumnAlias rightAlias = columnAliasBuilder.build().orElseThrow();
        final AliasLop aliasPushedDownDependentJoin = new AliasLop(pushedDownDependentJoin, rightAlias);

        return new InnerJoinLop(t1,
                                aliasPushedDownDependentJoin,
                                buildRewrittenJoinJoinSpecification(t1, aliasPushedDownDependentJoin, predicate),
                                innerJoinOutputSchema(t1.schema(), pushedDownDependentJoin.schema()));
    }

    private DistinctLop magicSet(LogicalOperator table, List<ColumnReference> projection) {
        return new DistinctLop(new ProjectionLop(table, projection));
    }

    private ValueExpression buildRewrittenJoinJoinSpecification(LogicalOperator left,
                                                                AliasLop aliasedRight,
                                                                ValueExpression dependentJoinPredicate) {
        final List<ValueExpression> expressions = new ArrayList<>();

        final Schema leftSchema = left.schema();
        final Schema rightSchema = aliasedRight.schema();
        final Alias rightAlias = aliasedRight.alias();

        for (ColumnReference columnName : leftSchema.columnNames()) {
            final UnqualifiedColumnReference unqualifiedColumnName = new UnqualifiedColumnReference(columnName.name());
            final ColumnReference rightColumnName = rightAlias.alias(unqualifiedColumnName);
            if (rightSchema.contains(rightColumnName)) {
                final ColumnMetadata match = rightSchema.column(rightColumnName);
                final Domain domain = match.domain();

                expressions.add(equalExpression(variable(columnName, domain), variable(match.name(), domain)));
            }
        }

        return collapseFilters(expressions)
                .<ValueExpression>map(naturalJoin -> andExpression(dependentJoinPredicate, naturalJoin))
                .orElse(dependentJoinPredicate);
    }
}
