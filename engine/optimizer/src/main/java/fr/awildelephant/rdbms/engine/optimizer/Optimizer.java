package fr.awildelephant.rdbms.engine.optimizer;

import fr.awildelephant.rdbms.engine.optimizer.optimization.FilterPushDown;
import fr.awildelephant.rdbms.engine.optimizer.optimization.JoinReordering;
import fr.awildelephant.rdbms.engine.optimizer.optimization.ProjectionPushDown;
import fr.awildelephant.rdbms.engine.optimizer.optimization.SimplifyExpressions;
import fr.awildelephant.rdbms.engine.optimizer.optimization.unnesting.SubqueryUnnesting;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.arithmetic.function.VariableCollector;

public class Optimizer {

    private final VariableCollector variableCollector = new VariableCollector();

    public LogicalOperator optimize(LogicalOperator plan) {
        LogicalOperator optimizedPlan = plan;
        /*
         * We push filters a first time, in order to transforms the cartesian products if possible.
         *
         * This is needed because we're going to alias the magic set when unnesting subqueries, but we should be able to push the filter down even to the aliased side
         * TODO: if filter is a = X and join condition is a = b, push the filter b = X down the right input
         */
        optimizedPlan = pushDownFilters(optimizedPlan);
        /* We're unable to push a dependent join down a table alias.
         * This "optimization" pushes down aliases as much as possible and transform table aliases to column aliases,
         * in an effort to get them out of the way. */
        optimizedPlan = new AliasSimplification().apply(optimizedPlan);
        optimizedPlan = unnestSubqueries(optimizedPlan);
        optimizedPlan = pushDownFilters(optimizedPlan);
        optimizedPlan = reorderJoins(optimizedPlan);
        optimizedPlan = pushDownProjections(optimizedPlan);
        optimizedPlan = pushDownFilters(optimizedPlan);
        // At the time of writing filters materialize their output but projections do not, so executing projections first is a no-brainer
        optimizedPlan = pushDownProjections(optimizedPlan);
        optimizedPlan = new SimplifyExpressions().apply(optimizedPlan);
        return optimizedPlan;
    }

    private LogicalOperator unnestSubqueries(LogicalOperator optimizedPlan) {
        return new SubqueryUnnesting(variableCollector).apply(optimizedPlan);
    }

    private LogicalOperator reorderJoins(LogicalOperator optimizedPlan) {
        return new JoinReordering(variableCollector).apply(optimizedPlan);
    }

    private LogicalOperator pushDownFilters(LogicalOperator optimizedPlan) {
        return new FilterPushDown(variableCollector).apply(optimizedPlan);
    }

    private LogicalOperator pushDownProjections(LogicalOperator operator) {
        return new ProjectionPushDown(variableCollector, operator.schema().columnNames()).apply(operator);
    }
}
