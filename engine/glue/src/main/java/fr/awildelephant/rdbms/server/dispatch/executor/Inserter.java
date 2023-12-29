package fr.awildelephant.rdbms.server.dispatch.executor;

import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.storage.data.column.VersionColumn;
import fr.awildelephant.rdbms.storage.data.table.ManagedTable;
import fr.awildelephant.rdbms.storage.data.table.Table;
import fr.awildelephant.rdbms.version.EndOfTimesVersion;
import fr.awildelephant.rdbms.version.TemporaryVersion;
import fr.awildelephant.rdbms.version.Version;

final class Inserter {

    private final Storage storage;

    Inserter(final Storage storage) {
        this.storage = storage;
    }

    void insert(final String tableName, final Table source, final TemporaryVersion version) {
        final ManagedTable destination = storage.get(tableName, version.databaseVersion());
        final Schema destinationSchema = destination.schema();
        checkCompatibility(source.schema(), destinationSchema);

        final int insertedCount = source.numberOfTuples();

        destination.addAll(source);

        fillTemporalColumns(version, destination, insertedCount);
    }

    private static void fillTemporalColumns(final TemporaryVersion version, final ManagedTable destination, final int insertedCount) {
        final VersionColumn fromColumn = destination.fromVersionColumn();
        final VersionColumn toColumn = destination.toVersionColumn();

        final int newNumberOfTuples = destination.numberOfTuples() + insertedCount;
        fromColumn.ensureCapacity(newNumberOfTuples);
        for (int i = 0; i < insertedCount; i++) {
            fromColumn.addGeneric(version);
        }

        final Version endOfTimes = EndOfTimesVersion.getInstance();
        toColumn.ensureCapacity(newNumberOfTuples);
        for (int i = 0; i < insertedCount; i++) {
            toColumn.addGeneric(endOfTimes);
        }
    }

    // TODO: more thorough check of compatibility, we should be able to cast source columns to their destination counterpart
    private static void checkCompatibility(final Schema source, final Schema destination) {
        // FIXME: dégueulasse
        checkColumnCount(source.numberOfAttributes(), destination.numberOfAttributes() - 2);
    }

    private static void checkColumnCount(final int actual, final int expected) {
        if (actual != expected) {
            throw new IllegalArgumentException("Column count mismatch: expected " + expected + " but got " + actual);
        }
    }
}
