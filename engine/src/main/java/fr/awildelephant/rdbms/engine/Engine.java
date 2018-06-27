package fr.awildelephant.rdbms.engine;

import fr.awildelephant.rdbms.engine.data.table.ListTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.plan.Plan;
import fr.awildelephant.rdbms.schema.Column;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class Engine {

    private final Map<String, Table> tables = new HashMap<>();

    public void createTable(final String tableName, final List<Column> attributes) {
        final Schema schema = new Schema(attributes);

        tables.put(tableName, new ListTable(schema));
    }

    public Optional<Table> getTable(final String tableName) {
        return Optional.ofNullable(tables.get(tableName));
    }

    public Table execute(final Plan logicalPlan) {
        return logicalPlan.accept(new PlanExecutor(tables));
    }
}
