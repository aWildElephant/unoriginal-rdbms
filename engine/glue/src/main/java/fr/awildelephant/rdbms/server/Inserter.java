package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.data.table.WriteableTable;
import fr.awildelephant.rdbms.schema.Schema;

final class Inserter {

    private final Storage storage;

    Inserter(Storage storage) {
        this.storage = storage;
    }

    void insert(String tableName, Table source) {
        final WriteableTable destination = storage.get(tableName);

        checkCompatibility(source.schema(), destination.schema());

        destination.addAll(source);
    }

    private static void checkCompatibility(Schema source, Schema destination) {
        checkColumnCount(source.numberOfAttributes(), destination.numberOfAttributes());
    }

    private static void checkColumnCount(int actual, int expected) {
        if (actual != expected) {
            throw new IllegalArgumentException("Column count mismatch: expected " + expected + " but got " + actual);
        }
    }
}
