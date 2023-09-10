package fr.awildelephant.rdbms.server.dispatch.executor;

import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.schema.ReservedKeywords;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.storage.data.column.AppendableColumn;
import fr.awildelephant.rdbms.storage.data.table.Table;
import fr.awildelephant.rdbms.storage.data.table.WriteableTable;
import fr.awildelephant.rdbms.version.EndOfTimesVersion;
import fr.awildelephant.rdbms.version.TemporaryVersion;
import fr.awildelephant.rdbms.version.Version;

import java.util.List;

import static fr.awildelephant.rdbms.data.value.VersionValue.versionValue;

final class Inserter {

    private final Storage storage;

    Inserter(Storage storage) {
        this.storage = storage;
    }

    void insert(String tableName, Table source, TemporaryVersion version) {
        final WriteableTable destination = storage.get(tableName, version.databaseVersion());
        final Schema destinationSchema = destination.schema();
        checkCompatibility(source.schema(), destinationSchema);

        final int insertedCount = source.numberOfTuples();

        destination.addAll(source);

        fillTemporalColumns(version, destination, insertedCount);
    }

    private static void fillTemporalColumns(TemporaryVersion version, WriteableTable destination, int insertedCount) {
        final Schema destinationSchema = destination.schema();
        final int fromColumnIndex = destinationSchema.column(ReservedKeywords.FROM_VERSION_COLUMN).index();
        final int toColumnIndex = destinationSchema.column(ReservedKeywords.TO_VERSION_COLUMN).index();

        final List<AppendableColumn> destinationColumns = destination.columns();
        final AppendableColumn fromColumn = destinationColumns.get(fromColumnIndex);
        final AppendableColumn toColumn = destinationColumns.get(toColumnIndex);

        final int newNumberOfTuples = destination.numberOfTuples() + insertedCount;
        fromColumn.ensureCapacity(newNumberOfTuples);
        for (int i = 0; i < insertedCount; i++) {
            fromColumn.add(versionValue(version));
        }

        final Version endOfTimes = EndOfTimesVersion.getInstance();
        toColumn.ensureCapacity(newNumberOfTuples);
        for (int i = 0; i < insertedCount; i++) {
            toColumn.add(versionValue(endOfTimes));
        }
    }

    // TODO: more thorough check of compatibility, we should be able to cast source columns to their destination counterpart
    private static void checkCompatibility(Schema source, Schema destination) {
        // FIXME: dégueulasse
        checkColumnCount(source.numberOfAttributes(), destination.numberOfAttributes() - 2);
    }

    private static void checkColumnCount(int actual, int expected) {
        if (actual != expected) {
            throw new IllegalArgumentException("Column count mismatch: expected " + expected + " but got " + actual);
        }
    }
}
