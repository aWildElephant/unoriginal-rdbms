package fr.awildelephant.rdbms.engine.optimizer;

import fr.awildelephant.rdbms.engine.optimizer.optimization.FilterPushDown;
import fr.awildelephant.rdbms.engine.optimizer.optimization.JoinReordering;
import fr.awildelephant.rdbms.engine.optimizer.optimization.ProjectionPushDown;
import fr.awildelephant.rdbms.engine.optimizer.optimization.SubqueryUnnesting;
import fr.awildelephant.rdbms.plan.LogicalOperator;

public class Optimizer {

    public LogicalOperator optimize(LogicalOperator plan) {
        LogicalOperator optimizedPlan = plan;
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
