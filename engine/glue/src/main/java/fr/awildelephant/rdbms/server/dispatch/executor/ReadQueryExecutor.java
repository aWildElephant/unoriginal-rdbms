package fr.awildelephant.rdbms.server.dispatch.executor;

import fr.awildelephant.rdbms.algebraizer.AlgebraizerFactory;
import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.engine.optimizer.Optimizer;
import fr.awildelephant.rdbms.execution.LogicalOperator;
import fr.awildelephant.rdbms.execution.ProjectionLop;
import fr.awildelephant.rdbms.execution.executor.SequentialPlanExecutor;
import fr.awildelephant.rdbms.execution.plan.Plan;
import fr.awildelephant.rdbms.execution.plan.PlanFactory;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.storage.data.table.Table;
import fr.awildelephant.rdbms.version.Version;

import java.util.List;

public final class ReadQueryExecutor {

    private final AlgebraizerFactory algebraizerFactory;
    private final Optimizer optimizer;
    private final PlanFactory planFactory;
    private final Storage storage;

    public ReadQueryExecutor(AlgebraizerFactory algebraizerFactory, Optimizer optimizer, PlanFactory planFactory, Storage storage) {
        this.algebraizerFactory = algebraizerFactory;
        this.optimizer = optimizer;
        this.planFactory = planFactory;
        this.storage = storage;
    }

    public Table execute(AST ast, Version readVersion) {
        final LogicalOperator rawPlan = algebraizerFactory.build(readVersion).apply(ast);
        final LogicalOperator optimizedPlan = optimizer.optimize(rawPlan);

        final List<ColumnReference> queryOutputColumns = rawPlan.schema().columnNames();
        final ProjectionLop fixedOptimizedPlan = new ProjectionLop(optimizedPlan, queryOutputColumns);

        final Plan physicalPlan = planFactory.apply(fixedOptimizedPlan);

        return new SequentialPlanExecutor(storage, readVersion).apply(physicalPlan);
    }
}
