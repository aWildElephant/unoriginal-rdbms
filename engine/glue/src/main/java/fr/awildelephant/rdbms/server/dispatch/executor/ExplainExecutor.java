package fr.awildelephant.rdbms.server.dispatch.executor;

import fr.awildelephant.rdbms.algebraizer.Algebraizer;
import fr.awildelephant.rdbms.ast.Explain;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.optimizer.Optimizer;
import fr.awildelephant.rdbms.explain.LogicalPlanTableBuilder;
import fr.awildelephant.rdbms.server.QueryContext;

public final class ExplainExecutor {

    private final Algebraizer algebraizer;
    private final LogicalPlanTableBuilder logicalPlanTableBuilder;
    private final Optimizer optimizer;

    public ExplainExecutor(Algebraizer algebraizer, LogicalPlanTableBuilder logicalPlanTableBuilder, Optimizer optimizer) {
        this.algebraizer = algebraizer;
        this.logicalPlanTableBuilder = logicalPlanTableBuilder;
        this.optimizer = optimizer;
    }

    public Table execute(Explain explain, QueryContext context) {
        return logicalPlanTableBuilder.explain(optimizer.optimize(algebraizer.apply(explain.child())));
    }
}
