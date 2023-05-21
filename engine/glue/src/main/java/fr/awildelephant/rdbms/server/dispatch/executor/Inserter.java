package fr.awildelephant.rdbms.server.dispatch.executor;

import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.database.version.Version;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.data.table.WriteableTable;
import fr.awildelephant.rdbms.schema.Schema;

final class Inserter {

    private final Storage storage;

    Inserter(Storage storage) {
        this.storage = storage;
    }

    void insert(String tableName, Table source, Version version) {
        final WriteableTable destination = storage.get(tableName, version);

        checkCompatibility(source.schema(), destination.schema());

        destination.addAll(source);
    }

    // TODO: more thorough check of compatibility, we should be able to cast source columns to their destination counterpart
    private static void checkCompatibility(Schema source, Schema destination) {
        checkColumnCount(source.numberOfAttributes(), destination.numberOfAttributes());
    }

    private static void checkColumnCount(int actual, int expected) {
        if (actual != expected) {
            throw new IllegalArgumentException("Column count mismatch: expected " + expected + " but got " + actual);
        }
    }
}
