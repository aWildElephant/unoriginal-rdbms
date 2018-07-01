package fr.awildelephant.rdbms.engine;

import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.plan.Plan;
import fr.awildelephant.rdbms.schema.Column;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;

public final class Engine {

    private final Map<String, Table> tables = new HashMap<>();

    public void create(final String tableName, final List<Column> columns) {
        final Schema schema = new Schema(columns);

        final Table table = simpleTable(schema);

        for (Column column : columns) {
            if (column.unique()) {
                table.createIndexOn(column.name());
            }
        }

        tables.put(tableName, table);
    }

    public void drop(String tableName) {
        final Table table = tables.remove(tableName);

        checkTableFound(table, tableName);
    }

    public boolean exists(String tableName) {
        return tables.containsKey(tableName);
    }

    public Table get(final String tableName) {
        final Table table = tables.get(tableName);

        checkTableFound(table, tableName);

        return table;
    }

    public Table execute(final Plan logicalPlan) {
        return logicalPlan.accept(new PlanExecutor(tables));
    }

    private void checkTableFound(Table table, String tableName) {
        if (table == null) {
            throw new IllegalArgumentException("Table not found: " + tableName);
        }
    }
}
