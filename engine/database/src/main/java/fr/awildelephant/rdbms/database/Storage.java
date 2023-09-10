package fr.awildelephant.rdbms.database;

import fr.awildelephant.rdbms.engine.data.table.ManagedTable;
import fr.awildelephant.rdbms.execution.BaseTableLop;
import fr.awildelephant.rdbms.execution.LogicalOperator;
import fr.awildelephant.rdbms.schema.TableNotFoundException;
import fr.awildelephant.rdbms.version.Version;
import fr.awildelephant.rdbms.version.VersionedObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// TODO: version storage
public final class Storage {

    private final Map<String, VersionedObject<ManagedTable>> tables = new ConcurrentHashMap<>();
    private final Map<String, VersionedObject<LogicalOperator>> views = new ConcurrentHashMap<>();

    public void create(final String tableName, final ManagedTable table, final Version version) {
        tables.put(tableName, VersionedObject.from(table, version));
    }

    public void createView(final String viewName, final LogicalOperator query, final Version version) {
        views.put(viewName, VersionedObject.from(query, version));
    }

    public void drop(final String tableName, final Version version) {
        tables.compute(tableName, (unused, versionedObject) -> {
            if (versionedObject != null && versionedObject.isValidAt(version)) {
                return versionedObject.endAt(version);
            }

            throw new TableNotFoundException(tableName);
        });
    }

    public boolean exists(String tableName, Version version) {
        final VersionedObject<ManagedTable> table = tables.get(tableName);
        return table != null && table.isValidAt(version);
    }

    public ManagedTable get(final String tableName, final Version version) {
        final VersionedObject<ManagedTable> table = tables.get(tableName);

        if (table == null || !table.isValidAt(version)) {
            throw new TableNotFoundException(tableName);
        }

        return table.object();
    }

    public LogicalOperator getOperator(final String name, final Version version) {
        final VersionedObject<ManagedTable> table = tables.get(name);

        if (table != null && table.isValidAt(version)) {
            return new BaseTableLop(name, table.object().schema().removeSystemColumns());
        }

        final VersionedObject<LogicalOperator> view = views.get(name);

        if (view != null && view.isValidAt(version)) {
            return view.object();
        }

        throw new TableNotFoundException(name);
    }
}
