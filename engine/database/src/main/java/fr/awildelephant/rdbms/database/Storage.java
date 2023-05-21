package fr.awildelephant.rdbms.database;

import fr.awildelephant.rdbms.engine.data.table.ManagedTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.execution.BaseTableLop;
import fr.awildelephant.rdbms.execution.LogicalOperator;
import fr.awildelephant.rdbms.schema.TableNotFoundException;

import java.util.HashMap;
import java.util.Map;

// TODO: version storage
public final class Storage {

    private final Map<String, ManagedTable> tables = new HashMap<>();
    private final Map<String, LogicalOperator> views = new HashMap<>();

    public void create(final String tableName, final ManagedTable table) {
        tables.put(tableName, table);
    }

    public void createView(String viewName, LogicalOperator query) {
        views.put(viewName, query);
    }

    public void drop(String tableName) {
        final Table table = tables.remove(tableName);

        checkTableFound(table, tableName);
    }

    public boolean exists(String tableName) {
        return tables.containsKey(tableName);
    }

    public ManagedTable get(final String tableName) {
        final ManagedTable table = tables.get(tableName);

        checkTableFound(table, tableName);

        return table;
    }

    public LogicalOperator getOperator(String name) {
        final ManagedTable table = tables.get(name);

        if (table != null) {
            return new BaseTableLop(name, table.schema());
        }

        final LogicalOperator view = views.get(name);

        if (view != null) {
            return view;
        }

        throw new TableNotFoundException(name);
    }

    private void checkTableFound(Table table, String tableName) {
        if (table == null) {
            throw new TableNotFoundException(tableName);
        }
    }
}
