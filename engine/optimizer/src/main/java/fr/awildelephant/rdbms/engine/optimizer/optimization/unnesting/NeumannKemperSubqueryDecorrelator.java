package fr.awildelephant.rdbms.engine.optimizer.optimization.unnesting;

import fr.awildelephant.rdbms.arithmetic.ExpressionHelper;
import fr.awildelephant.rdbms.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.engine.optimizer.FreeVariableAliasing;
import fr.awildelephant.rdbms.function.VariableCollector;
import fr.awildelephant.rdbms.operator.logical.AliasLop;
import fr.awildelephant.rdbms.operator.logical.CartesianProductLop;
import fr.awildelephant.rdbms.operator.logical.DependentJoinLop;
import fr.awildelephant.rdbms.operator.logical.DependentSemiJoinLop;
import fr.awildelephant.rdbms.operator.logical.DistinctLop;
import fr.awildelephant.rdbms.operator.logical.InnerJoinLop;
import fr.awildelephant.rdbms.operator.logical.LogicalOperator;
import fr.awildelephant.rdbms.operator.logical.ProjectionLop;
import fr.awildelephant.rdbms.operator.logical.SemiJoinLop;
import fr.awildelephant.rdbms.operator.logical.alias.Alias;
import fr.awildelephant.rdbms.operator.logical.alias.ExactMatchAlias;
import fr.awildelephant.rdbms.operator.logical.alias.ReversibleAlias;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.QualifiedColumnReference;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static fr.awildelephant.rdbms.arithmetic.AndExpression.andExpression;
import static fr.awildelephant.rdbms.arithmetic.EqualExpression.equalExpression;
import static fr.awildelephant.rdbms.arithmetic.Variable.variable;
import static fr.awildelephant.rdbms.engine.optimizer.util.AttributesFunction.attributes;
import static fr.awildelephant.rdbms.engine.optimizer.util.FreeVariablesFunction.freeVariables;
import static fr.awildelephant.rdbms.engine.optimizer.util.SetHelper.intersection;
import static fr.awildelephant.rdbms.execution.filter.FilterCollapser.collapseFilters;

/**
 * Decorrelate subqueries using the method described in "Unnesting Arbitrary Queries" by Thomas Neumann and Alfons Kemper.
 * <p>
 * NeumannKemperSubqueryDecorrelator sounds cool, but the paper doesn't say the method should be named that way.
 */
public final class NeumannKemperSubqueryDecorrelator {

    private final VariableCollector variableCollector;

    public NeumannKemperSubqueryDecorrelator(VariableCollector variableCollector) {
        this.variableCollector = variableCollector;
    }

    public LogicalOperator decorrelate(DependentJoinLop plan) {
        final LogicalOperator t1 = plan.left();
        final LogicalOperator t2 = plan.right();

        final Set<ColumnReference> correlatedVariables = intersection(freeVariables(t2), attributes(t1));
        if (correlatedVariables.isEmpty()) {
            if (plan.predicate() != null) {
                return new InnerJoinLop(t1, t2, plan.predicate());
            } else {
                return new CartesianProductLop(t1, t2);
            }
        }

        final LogicalOperator rewrittenJoin = rewriteDependentInnerJoin(t1, t2, plan.predicate(), correlatedVariables);

        return new DependentJoinPushDown(variableCollector).apply(rewrittenJoin);
    }

    private LogicalOperator rewriteDependentInnerJoin(LogicalOperator t1,
                                                      LogicalOperator t2,
                                                      ValueExpression predicate,
                                                      Collection<ColumnReference> correlatedColumns) {
        final DistinctLop magicSet = magicSet(t1, new ArrayList<>(correlatedColumns));

        final AliasLop aliasedMagicSet = randomAliasing(magicSet);
        final Alias magicSetAlias = aliasedMagicSet.alias();

        final LogicalOperator t2WithAliasedOuterQueryVariables = new FreeVariableAliasing(magicSetAlias).apply(t2);

        final DependentJoinLop pushedDownDependentJoin = new DependentJoinLop(aliasedMagicSet,
                t2WithAliasedOuterQueryVariables);

        return new InnerJoinLop(t1,
                pushedDownDependentJoin,
                buildRewrittenJoinJoinSpecification(t1, aliasedMagicSet, predicate));
    }

    public LogicalOperator decorrelate(DependentSemiJoinLop plan) {
        final LogicalOperator t1 = plan.left();
        final LogicalOperator t2 = plan.right();

        final Set<ColumnReference> correlatedVariables = intersection(freeVariables(t2), attributes(t1));
        if (correlatedVariables.isEmpty()) {
            return new SemiJoinLop(t1, t2, plan.predicate(), plan.outputColumnName());
        }

        final LogicalOperator rewrittenJoin = rewriteDependentSemiJoin(t1,
                t2,
                plan.predicate(),
                correlatedVariables,
                plan.outputColumnName());

        return new DependentJoinPushDown(variableCollector).apply(rewrittenJoin);
    }

    private LogicalOperator rewriteDependentSemiJoin(LogicalOperator t1,
                                                     LogicalOperator t2,
                                                     ValueExpression predicate,
                                                     Collection<ColumnReference> correlatedColumns,
                                                     ColumnReference outputColumnName) {
        final DistinctLop magicSet = magicSet(t1, new ArrayList<>(correlatedColumns));

        final AliasLop aliasedMagicSet = randomAliasing(magicSet);

        final Alias magicSetAlias = aliasedMagicSet.alias();

        final LogicalOperator t2WithAliasedOuterQueryVariables = new FreeVariableAliasing(magicSetAlias).apply(t2);

        final DependentJoinLop pushedDownDependentJoin = new DependentJoinLop(aliasedMagicSet,
                t2WithAliasedOuterQueryVariables);

        return new SemiJoinLop(t1,
                pushedDownDependentJoin,
                buildRewrittenJoinJoinSpecification(t1, aliasedMagicSet, predicate),
                outputColumnName);
    }

    private AliasLop randomAliasing(LogicalOperator node) {
        final Map<String, String> tableAliases = new HashMap<>();

        final Map<ColumnReference, ColumnReference> aliasing = new HashMap<>();
        for (ColumnReference column : node.schema().columnNames()) {
            final Optional<String> table = column.table();

            if (table.isPresent()) {
                final String tableAlias = tableAliases.computeIfAbsent(table.get(),
                        unused -> UUID.randomUUID().toString());

                aliasing.put(column, new QualifiedColumnReference(tableAlias, column.name()));
            } else {
                aliasing.put(column, new UnqualifiedColumnReference(column.name()));
            }
        }

        return new AliasLop(node, new ExactMatchAlias(aliasing));
    }

    private DistinctLop magicSet(LogicalOperator table, List<ColumnReference> projection) {
        return new DistinctLop(new ProjectionLop(table, projection));
    }

    private ValueExpression buildRewrittenJoinJoinSpecification(LogicalOperator left,
                                                                AliasLop aliasedMagicSet,
                                                                ValueExpression dependentJoinPredicate) {
        final List<ValueExpression> expressions = new ArrayList<>();

        final Schema leftSchema = left.schema();
        final Schema rightSchema = aliasedMagicSet.schema();
        final ReversibleAlias magicSetAlias = aliasedMagicSet.alias();

        for (ColumnReference rightColumnName : rightSchema.columnNames()) {
            final ColumnReference unaliasedRightColumn = magicSetAlias.unalias(rightColumnName);

            final ColumnMetadata leftColumn = leftSchema.column(unaliasedRightColumn).metadata();
            final Domain domain = leftColumn.domain();

            expressions.add(equalExpression(variable(leftColumn.name(), domain), variable(rightColumnName, domain)));
        }

        final Optional<ValueExpression> naturalJoinPredicate = collapseFilters(expressions);

        if (dependentJoinPredicate != null) {
            if (naturalJoinPredicate.isPresent()) {
                return andExpression(dependentJoinPredicate, naturalJoinPredicate.get());
            } else {
                return dependentJoinPredicate;
            }
        } else {
            return naturalJoinPredicate.orElseGet(ExpressionHelper::alwaysTrue);
        }
    }
}
