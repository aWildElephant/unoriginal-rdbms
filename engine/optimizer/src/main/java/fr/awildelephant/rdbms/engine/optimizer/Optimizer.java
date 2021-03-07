package fr.awildelephant.rdbms.engine.optimizer;

import fr.awildelephant.rdbms.engine.optimizer.optimization.FilterPushDown;
import fr.awildelephant.rdbms.engine.optimizer.optimization.JoinReordering;
import fr.awildelephant.rdbms.engine.optimizer.optimization.ProjectionPushDown;
import fr.awildelephant.rdbms.engine.optimizer.optimization.unnesting.SubqueryUnnesting;
import fr.awildelephant.rdbms.plan.LogicalOperator;

public class Optimizer {

    public LogicalOperator optimize(LogicalOperator plan) {
        LogicalOperator optimizedPlan = plan;
        /*
         * We push filters a first time, in order to transforms the cartesian products if possible.
         *
         * This is needed because we're going to alias the magic set when unnesting subqueries, but we should be able to push the filter down even to the aliased side
         * TODO: if filter is a = X and join condition is a = b, push the filter b = X down the right input
         */
        optimizedPlan = new FilterPushDown().apply(optimizedPlan);
        /* We're unable to push a dependent join down a table alias.
         * This "optimization" pushes down aliases as much as possible and transform table aliases to column aliases,
         * in an effort to get them out of the way. */
        optimizedPlan = new AliasSimplification().apply(optimizedPlan);
        optimizedPlan = new SubqueryUnnesting().apply(optimizedPlan);
        optimizedPlan = new FilterPushDown().apply(optimizedPlan);
        optimizedPlan = new JoinReordering().apply(optimizedPlan);
        optimizedPlan = ProjectionPushDown.pushDownProjections(optimizedPlan);
        optimizedPlan = new FilterPushDown().apply(optimizedPlan);
        // At the time of writing filters materialize their output but projections do not, so executing projections first is a no-brainer
        optimizedPlan = ProjectionPushDown.pushDownProjections(optimizedPlan);
        return optimizedPlan;
    }
}
