package fr.awildelephant.rdbms.engine.optimizer;

import fr.awildelephant.rdbms.engine.optimizer.optimization.FilterPushDown;
import fr.awildelephant.rdbms.engine.optimizer.optimization.SubqueryUnnesting;
import fr.awildelephant.rdbms.plan.LogicalOperator;

public class Optimizer {

    public LogicalOperator optimize(LogicalOperator plan) {
        final LogicalOperator moncul = new SubqueryUnnesting().apply(plan);
        return new FilterPushDown().apply(moncul);
    }
}
