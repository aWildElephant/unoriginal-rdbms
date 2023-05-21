package fr.awildelephant.rdbms.database;

import fr.awildelephant.rdbms.database.version.Version;
import fr.awildelephant.rdbms.engine.data.table.ManagedTable;
import fr.awildelephant.rdbms.execution.LogicalOperator;

public final class StorageSnapshot {

    private final Storage storage;
    private final Version version;

    public StorageSnapshot(Storage storage, Version version) {
        this.storage = storage;
        this.version = version;
    }

    public boolean exists(String tableName) {
        return storage.exists(tableName, version);
    }

    public ManagedTable get(final String tableName) {
        return storage.get(tableName, version);
    }

    public LogicalOperator getOperator(String name) {
        return storage.getOperator(name, version);
    }
}
