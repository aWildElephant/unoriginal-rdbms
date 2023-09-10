package fr.awildelephant.rdbms.database;

import fr.awildelephant.rdbms.version.PermanentVersion;
import fr.awildelephant.rdbms.version.TemporaryVersion;

import java.util.concurrent.atomic.AtomicReference;

public final class MVCC {

    private final AtomicReference<PermanentVersion> currentVersion = new AtomicReference<>(new PermanentVersion(Long.MIN_VALUE));

    public PermanentVersion currentVersion() {
        return currentVersion.get();
    }

    public boolean commit(TemporaryVersion temporaryVersion) {
        final PermanentVersion databaseVersion = temporaryVersion.databaseVersion();
        final PermanentVersion newVersion = databaseVersion.next();

        final boolean committed = currentVersion.compareAndSet(databaseVersion, newVersion);
        if (committed) {
            temporaryVersion.commit(newVersion);
        } else {
            temporaryVersion.rollBack();
        }

        return committed;
    }
}
