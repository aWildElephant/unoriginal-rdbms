package fr.awildelephant.rdbms.server.dispatch.executor;

import fr.awildelephant.rdbms.algebraizer.Algebraizer;
import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.optimizer.Optimizer;
import fr.awildelephant.rdbms.execution.LogicalOperator;
import fr.awildelephant.rdbms.execution.ProjectionLop;
import fr.awildelephant.rdbms.execution.executor.SequentialPlanExecutor;
import fr.awildelephant.rdbms.execution.plan.Plan;
import fr.awildelephant.rdbms.execution.plan.PlanFactory;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.server.QueryContext;

import java.util.List;

public final class ReadQueryExecutor {

    private final Algebraizer algebraizer;
    private final Optimizer optimizer;
    private final PlanFactory planFactory;
    private final Storage storage;

    public ReadQueryExecutor(Algebraizer algebraizer, Optimizer optimizer, PlanFactory planFactory, Storage storage) {
        this.algebraizer = algebraizer;
        this.optimizer = optimizer;
        this.planFactory = planFactory;
        this.storage = storage;
    }

    // TODO: use context's read timestamp
    public Table execute(AST ast, QueryContext context) {
        final LogicalOperator rawPlan = algebraizer.apply(ast);
        final LogicalOperator optimizedPlan = optimizer.optimize(rawPlan);

        final List<ColumnReference> queryOutputColumns = rawPlan.schema().columnNames();
        final ProjectionLop fixedOptimizedPlan = new ProjectionLop(optimizedPlan, queryOutputColumns);

        final Plan physicalPlan = planFactory.apply(fixedOptimizedPlan);

        return new SequentialPlanExecutor().apply(storage, physicalPlan);
    }
}
