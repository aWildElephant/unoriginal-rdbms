package fr.awildelephant.rdbms.engine;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.data.table.TableWithChecker;
import fr.awildelephant.rdbms.plan.LogicalOperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class Engine {

    private final Map<String, TableWithChecker> tables = new HashMap<>();

    public void create(final String tableName, final TableWithChecker table) {
        tables.put(tableName, table);
    }

    public void drop(String tableName) {
        final Table table = tables.remove(tableName);

        checkTableFound(table, tableName);
    }

    public boolean exists(String tableName) {
        return tables.containsKey(tableName);
    }

    public TableWithChecker get(final String tableName) {
        final TableWithChecker table = tables.get(tableName);

        checkTableFound(table, tableName);

        return table;
    }

    public Table execute(final LogicalOperator logicalPlan) {
        // TODO: will not work with concurrency
        final Optional<Table> result = logicalPlan.accept(new PlanExecutor(tables)).reduce((first, second) -> {
            for (Record record : second) {
                first.add(record);
            }

            return first;
        });

        if (!result.isPresent()) {
            throw new IllegalStateException();
        }

        return result.get();
    }

    private void checkTableFound(Table table, String tableName) {
        if (table == null) {
            throw new IllegalArgumentException("Table not found: " + tableName);
        }
    }
}
