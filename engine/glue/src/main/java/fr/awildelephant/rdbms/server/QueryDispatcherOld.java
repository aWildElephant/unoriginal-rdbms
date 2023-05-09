package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.algebraizer.Algebraizer;
import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Distinct;
import fr.awildelephant.rdbms.ast.Explain;
import fr.awildelephant.rdbms.ast.Limit;
import fr.awildelephant.rdbms.ast.Select;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.ast.Truncate;
import fr.awildelephant.rdbms.ast.Values;
import fr.awildelephant.rdbms.ast.With;
import fr.awildelephant.rdbms.ast.visitor.DefaultASTVisitor;
import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.optimizer.Optimizer;
import fr.awildelephant.rdbms.execution.LogicalOperator;
import fr.awildelephant.rdbms.execution.ProjectionLop;
import fr.awildelephant.rdbms.execution.executor.SequentialPlanExecutor;
import fr.awildelephant.rdbms.execution.plan.Plan;
import fr.awildelephant.rdbms.execution.plan.PlanFactory;
import fr.awildelephant.rdbms.explain.LogicalPlanTableBuilder;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.server.with.WithInliner;
import fr.awildelephant.rdbms.server.with.WithInlinerFactory;

import java.util.List;

public class QueryDispatcherOld extends DefaultASTVisitor<Table> {

    private final Storage storage;
    private final Algebraizer algebraizer;
    private final Optimizer optimizer;
    private final WithInliner<Table> withInliner;
    private final LogicalPlanTableBuilder logicalPlanTableBuilder;
    private final PlanFactory planFactory;

    QueryDispatcherOld(Storage storage, Algebraizer algebraizer, Optimizer optimizer,
                       WithInlinerFactory withInlinerFactory, LogicalPlanTableBuilder logicalPlanTableBuilder,
                       PlanFactory planFactory) {
        this.storage = storage;
        this.algebraizer = algebraizer;
        this.optimizer = optimizer;
        this.withInliner = withInlinerFactory.build(this);
        this.logicalPlanTableBuilder = logicalPlanTableBuilder;
        this.planFactory = planFactory;
    }

    @Override
    public Table visit(Explain explain) {
        return logicalPlanTableBuilder.explain(optimizer.optimize(algebraizer.apply(explain.child())));
    }

    @Override
    public Table visit(Distinct distinct) {
        return executeReadQuery(distinct);
    }

    @Override
    public Table visit(Limit limit) {
        return executeReadQuery(limit);
    }

    @Override
    public Table visit(Select select) {
        return executeReadQuery(select);
    }

    @Override
    public Table visit(TableName tableName) {
        return executeReadQuery(tableName);
    }

    @Override
    public Table visit(Truncate truncate) {
        throw new UnsupportedOperationException("TRUNCATE statement not yet implemented");
    }

    @Override
    public Table visit(Values values) {
        return executeReadQuery(values);
    }

    @Override
    public Table visit(With with) {
        return withInliner.apply(with);
    }

    private Table executeReadQuery(AST ast) {
        final LogicalOperator rawPlan = algebraizer.apply(ast);
        final LogicalOperator optimizedPlan = optimizer.optimize(rawPlan);

        final List<ColumnReference> queryOutputColumns = rawPlan.schema().columnNames();
        final ProjectionLop fixedOptimizedPlan = new ProjectionLop(optimizedPlan, queryOutputColumns);

        final Plan physicalPlan = planFactory.apply(fixedOptimizedPlan);

        return new SequentialPlanExecutor().apply(storage, physicalPlan);
    }

    @Override
    public Table defaultVisit(AST node) {
        throw new IllegalStateException();
    }

}
