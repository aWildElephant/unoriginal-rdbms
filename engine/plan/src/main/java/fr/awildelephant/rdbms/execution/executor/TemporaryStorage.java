package fr.awildelephant.rdbms.execution.executor;

import fr.awildelephant.rdbms.database.StorageSnapshot;
import fr.awildelephant.rdbms.storage.data.table.ManagedTable;
import fr.awildelephant.rdbms.storage.data.table.Table;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TemporaryStorage {

    private final StorageSnapshot permanentStorageSnapshot;
    private final Map<String, Table> temporaryComponent;

    public TemporaryStorage(StorageSnapshot permanentStorageSnapshot) {
        this.permanentStorageSnapshot = permanentStorageSnapshot;
        this.temporaryComponent = new HashMap<>();
    }

    public ManagedTable getBaseTable(final String tableName) {
        return permanentStorageSnapshot.get(tableName);
    }

    public Table get(String key) {
        return Optional.ofNullable(temporaryComponent.get(key)).orElseGet(() -> permanentStorageSnapshot.get(key));
    }

    public void storeTemporaryResult(String key, Table table) {
        if (permanentStorageSnapshot.exists(key)) {
            throw new IllegalArgumentException(); // TODO: proper exception
        }

        temporaryComponent.put(key, table);
    }

    public void freeTemporaryResult(String key) {
        temporaryComponent.remove(key);
    }
}
