package fr.awildelephant.rdbms.database;

import fr.awildelephant.rdbms.database.version.PermanentVersion;
import fr.awildelephant.rdbms.database.version.TemporaryVersion;

import java.util.concurrent.atomic.AtomicReference;

public final class MVCC {

    private final AtomicReference<PermanentVersion> currentVersion = new AtomicReference<>(PermanentVersion.BEGINNING_OF_TIMES);

    public PermanentVersion currentVersion() {
        return currentVersion.get();
    }

    public boolean commit(TemporaryVersion temporaryVersion) {
        final PermanentVersion newVersion = temporaryVersion.databaseVersion().next();

        final boolean committed = currentVersion.compareAndSet(temporaryVersion.databaseVersion(), newVersion);
        if (committed) {
            temporaryVersion.commit(newVersion);
        } else {
            temporaryVersion.rollBack();
        }

        return committed;
    }
}
