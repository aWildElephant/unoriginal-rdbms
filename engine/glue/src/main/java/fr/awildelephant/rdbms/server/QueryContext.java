package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.database.version.TemporaryVersion;

public final class QueryContext {

    private TemporaryVersion temporaryVersion;
    private boolean update;

    public TemporaryVersion temporaryVersion() {
        return temporaryVersion;
    }

    public void temporaryVersion(TemporaryVersion temporaryVersion) {
        this.temporaryVersion = temporaryVersion;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate() {
        this.update = true;
    }
}
