package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.algebraizer.Algebraizer;
import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.CreateTable;
import fr.awildelephant.rdbms.ast.CreateView;
import fr.awildelephant.rdbms.ast.Distinct;
import fr.awildelephant.rdbms.ast.DropTable;
import fr.awildelephant.rdbms.ast.Explain;
import fr.awildelephant.rdbms.ast.InsertInto;
import fr.awildelephant.rdbms.ast.Limit;
import fr.awildelephant.rdbms.ast.Select;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.ast.Values;
import fr.awildelephant.rdbms.ast.With;
import fr.awildelephant.rdbms.ast.visitor.DefaultASTVisitor;
import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.engine.data.table.ManagedTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.optimizer.Optimizer;
import fr.awildelephant.rdbms.execution.AliasLop;
import fr.awildelephant.rdbms.execution.LogicalOperator;
import fr.awildelephant.rdbms.execution.ProjectionLop;
import fr.awildelephant.rdbms.execution.alias.ColumnAliasBuilder;
import fr.awildelephant.rdbms.execution.executor.SequentialPlanExecutor;
import fr.awildelephant.rdbms.execution.plan.Plan;
import fr.awildelephant.rdbms.execution.plan.PlanFactory;
import fr.awildelephant.rdbms.explain.LogicalPlanTableBuilder;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.server.with.WithInliner;
import fr.awildelephant.rdbms.server.with.WithInlinerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static fr.awildelephant.rdbms.server.Inserter.insertRows;
import static fr.awildelephant.rdbms.server.TableCreator.tableFrom;

public class QueryDispatcher extends DefaultASTVisitor<Table> {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Storage storage;
    private final Algebraizer algebraizer;
    private final Optimizer optimizer;
    private final WithInliner<Table> withInliner;
    private final LogicalPlanTableBuilder logicalPlanTableBuilder;

    QueryDispatcher(Storage storage,
                    Algebraizer algebraizer,
                    Optimizer optimizer,
                    WithInlinerFactory withInlinerFactory, LogicalPlanTableBuilder logicalPlanTableBuilder) {
        this.storage = storage;
        this.algebraizer = algebraizer;
        this.optimizer = optimizer;
        this.withInliner = withInlinerFactory.build(this);
        this.logicalPlanTableBuilder = logicalPlanTableBuilder;
    }

    @Override
    public Table visit(CreateTable createTable) {
        final String tableName = createTable.tableName().name();

        checkTableDoesNotExist(tableName);

        storage.create(tableName, tableFrom(createTable));

        return null;
    }

    @Override
    public Table visit(CreateView createView) {
        final LogicalOperator query = algebraizer.apply(createView.query());

        final List<String> columnNames = createView.columnNames();
        final Schema querySchema = query.schema();
        if (querySchema.numberOfAttributes() != columnNames.size()) {
            throw new IllegalStateException();
        }

        final ColumnAliasBuilder columnAliasBuilder = new ColumnAliasBuilder();

        final List<ColumnReference> queryOutputColumns = querySchema.columnNames();
        for (int i = 0; i < queryOutputColumns.size(); i++) {
            columnAliasBuilder.add(queryOutputColumns.get(i), columnNames.get(i));
        }

        storage.createView(createView.name(), new AliasLop(query, columnAliasBuilder.build().orElseThrow()));

        return null;
    }

    @Override
    public Table visit(DropTable dropTable) {
        storage.drop(dropTable.tableName().name());

        return null;
    }

    @Override
    public Table visit(Explain explain) {
        return logicalPlanTableBuilder.explain(optimizer.optimize(algebraizer.apply(explain.child())));
    }

    @Override
    public Table visit(InsertInto insertInto) {
        final ManagedTable table = storage.get(insertInto.targetTable().name());

        insertRows(insertInto, table);

        return null;
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
    public Table visit(Values values) {
        return executeReadQuery(values);
    }

    @Override
    public Table visit(With with) {
        return withInliner.apply(with);
    }

    private Table executeReadQuery(AST ast) {
        LOGGER.debug("Executing {}", ast);
        final LogicalOperator rawPlan = algebraizer.apply(ast);
        final LogicalOperator optimizedPlan = optimizer.optimize(rawPlan);

        final List<ColumnReference> queryOutputColumns = rawPlan.schema().columnNames();
        final ProjectionLop fixedOptimizedPlan = new ProjectionLop(optimizedPlan, queryOutputColumns);

        final Plan physicalPlan = PlanFactory.build(fixedOptimizedPlan);

        return new SequentialPlanExecutor().apply(storage, physicalPlan);
    }

    @Override
    public Table defaultVisit(AST node) {
        throw new IllegalStateException();
    }

    private void checkTableDoesNotExist(String tableName) {
        if (storage.exists(tableName)) {
            throw new IllegalArgumentException("Table " + tableName + " already exists");
        }
    }
}
