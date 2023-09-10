package fr.awildelephant.rdbms.server.dispatch.executor;

import fr.awildelephant.rdbms.algebraizer.Algebraizer;
import fr.awildelephant.rdbms.algebraizer.AlgebraizerFactory;
import fr.awildelephant.rdbms.ast.Explain;
import fr.awildelephant.rdbms.engine.optimizer.Optimizer;
import fr.awildelephant.rdbms.explain.LogicalPlanTableBuilder;
import fr.awildelephant.rdbms.server.QueryContext;
import fr.awildelephant.rdbms.storage.data.table.Table;

public final class ExplainExecutor {

    private final AlgebraizerFactory algebraizerFactory;
    private final LogicalPlanTableBuilder logicalPlanTableBuilder;
    private final Optimizer optimizer;

    public ExplainExecutor(AlgebraizerFactory algebraizer, LogicalPlanTableBuilder logicalPlanTableBuilder, Optimizer optimizer) {
        this.algebraizerFactory = algebraizer;
        this.logicalPlanTableBuilder = logicalPlanTableBuilder;
        this.optimizer = optimizer;
    }

    public Table execute(Explain explain, QueryContext context) {
        final Algebraizer algebraizer = algebraizerFactory.build(context.temporaryVersion().databaseVersion());
        return logicalPlanTableBuilder.explain(optimizer.optimize(algebraizer.apply(explain.child())));
    }
}
