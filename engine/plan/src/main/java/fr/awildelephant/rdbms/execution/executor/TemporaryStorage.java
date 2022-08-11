package fr.awildelephant.rdbms.execution.executor;

import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.engine.data.table.Table;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TemporaryStorage {

    private final Storage permanentStorage;
    private final Map<String, Table> temporaryComponent;

    public TemporaryStorage(Storage permanentStorage) {
        this.permanentStorage = permanentStorage;
        this.temporaryComponent = new HashMap<>();
    }

    public Table get(String key) {
        return Optional.ofNullable(temporaryComponent.get(key)).orElseGet(() -> permanentStorage.get(key));
    }

    public void storeTemporaryResult(String key, Table table) {
        if (permanentStorage.exists(key)) {
            throw new IllegalArgumentException(); // TODO: proper exception
        }

        temporaryComponent.put(key, table);
    }

    public void freeTemporaryResult(String key) {
        temporaryComponent.remove(key);
    }
}
