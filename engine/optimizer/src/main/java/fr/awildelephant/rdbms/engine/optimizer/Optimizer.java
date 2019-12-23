package fr.awildelephant.rdbms.engine.optimizer;

import fr.awildelephant.rdbms.engine.optimizer.optimization.FilterPushDown;
import fr.awildelephant.rdbms.engine.optimizer.optimization.JoinReordering;
import fr.awildelephant.rdbms.engine.optimizer.optimization.SubqueryUnnesting;
import fr.awildelephant.rdbms.plan.LogicalOperator;

import java.util.function.Function;

public class Optimizer {

    public LogicalOperator optimize(LogicalOperator plan) {
        final Function<LogicalOperator, LogicalOperator> optimizationChain = new SubqueryUnnesting()
                .andThen(new FilterPushDown())
                .andThen(new JoinReordering());

        return optimizationChain.apply(plan);
    }
}
