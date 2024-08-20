package fr.awildelephant.rdbms.server.dispatch.executor;

import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.storage.data.column.VersionColumn;
import fr.awildelephant.rdbms.storage.data.table.ManagedTable;
import fr.awildelephant.rdbms.storage.data.table.Table;
import fr.awildelephant.rdbms.version.EndOfTimesVersion;
import fr.awildelephant.rdbms.version.TemporaryVersion;
import fr.awildelephant.rdbms.version.Version;

import java.util.List;

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

    private static void checkCompatibility(final Schema source, final Schema destination) {
        checkColumnCount(source.numberOfAttributes(), destination.numberOfAttributes() - 2);

        checkColumnTypes(source, destination);
    }

    private static void checkColumnCount(final int actual, final int expected) {
        if (actual != expected) {
            throw new IllegalArgumentException("Column count mismatch: expected " + expected + " but got " + actual);
        }
    }

    private static void checkColumnTypes(final Schema source, final Schema destination) {
        final List<ColumnReference> sourceColumnNames = source.columnNames();
        final List<ColumnReference> destinationColumnNames = destination.columnNames();

        for (int i = 0; i < sourceColumnNames.size(); i++) {
            final ColumnMetadata sourceColumn = source.column(sourceColumnNames.get(i)).metadata();
            final ColumnMetadata destinationColumn = destination.column(destinationColumnNames.get(i)).metadata();

            final Domain sourceDomain = sourceColumn.domain();
            final Domain destinationDomain = destinationColumn.domain();
            if (!sourceDomain.canBeUsedAs(destinationDomain)) {
                throw new IllegalArgumentException(String.format("Column '%s': type %s is incompatible with type %s", destinationColumn.name(), sourceDomain.name(), destinationDomain.name()));
            }
        }
    }
}
