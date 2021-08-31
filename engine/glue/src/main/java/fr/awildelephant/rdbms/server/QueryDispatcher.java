package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.algebraizer.Algebraizer;
import fr.awildelephant.rdbms.ast.*;
import fr.awildelephant.rdbms.ast.visitor.DefaultASTVisitor;
import fr.awildelephant.rdbms.engine.Storage;
import fr.awildelephant.rdbms.engine.data.column.TextColumn;
import fr.awildelephant.rdbms.engine.data.table.ColumnBasedTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.optimizer.Optimizer;
import fr.awildelephant.rdbms.explain.PlanJsonBuilder;
import fr.awildelephant.rdbms.plan.AliasLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.ProjectionLop;
import fr.awildelephant.rdbms.plan.alias.ColumnAliasBuilder;
import fr.awildelephant.rdbms.schema.*;
import fr.awildelephant.rdbms.server.with.WithInliner;
import fr.awildelephant.rdbms.server.with.WithInlinerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static fr.awildelephant.rdbms.data.value.TextValue.textValue;
import static fr.awildelephant.rdbms.server.Inserter.insertRows;
import static fr.awildelephant.rdbms.server.TableCreator.tableFrom;

public class QueryDispatcher extends DefaultASTVisitor<Table> {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Storage storage;
    private final Algebraizer algebraizer;
    private final Optimizer optimizer;
    private final WithInliner<Table> withInliner;

    QueryDispatcher(Storage storage,
                    Algebraizer algebraizer,
                    Optimizer optimizer,
                    WithInlinerFactory withInlinerFactory) {
        this.storage = storage;
        this.algebraizer = algebraizer;
        this.optimizer = optimizer;
        this.withInliner = withInlinerFactory.build(this);
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
        final LogicalOperator plan = optimizer.optimize(algebraizer.apply(explain.input()));
        // TODO: if possible, instantiate once
        final PlanJsonBuilder planJsonBuilder = new PlanJsonBuilder();
        planJsonBuilder.apply(plan);

        return explainTable(planJsonBuilder.toString());
    }

    private Table explainTable(String explanation) {
        final Schema schema = new Schema(List.of(new ColumnMetadata(0,
                new UnqualifiedColumnReference("plan"),
                Domain.TEXT,
                true,
                false)));

        final TextColumn column = new TextColumn(1);
        column.add(textValue(explanation));

        return new ColumnBasedTable(schema, List.of(column));
    }

    @Override
    public Table visit(InsertInto insertInto) {
        final Table table = storage.get(insertInto.targetTable().name());

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
        return storage.execute(new ProjectionLop(optimizedPlan, queryOutputColumns));
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
