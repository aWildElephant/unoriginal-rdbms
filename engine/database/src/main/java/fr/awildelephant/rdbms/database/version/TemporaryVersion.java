package fr.awildelephant.rdbms.database.version;

import fr.awildelephant.rdbms.database.TransactionId;

public class TemporaryVersion implements Version {

    private final PermanentVersion databaseVersion;
    private final TransactionId transactionId;

    private PermanentVersion permanentVersion = PermanentVersion.END_OF_TIMES;
    private boolean rolledBack;

    public TemporaryVersion(PermanentVersion databaseVersion, TransactionId transactionId) {
        this.databaseVersion = databaseVersion;
        this.transactionId = transactionId;
    }

    public PermanentVersion databaseVersion() {
        return databaseVersion;
    }

    @Override
    public PermanentVersion permanentVersion() {
        return permanentVersion;
    }

    public void commit(PermanentVersion permanentVersion) {
        this.permanentVersion = permanentVersion;
    }

    public void rollBack() {
        rolledBack = true;
    }
}
