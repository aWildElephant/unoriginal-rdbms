package fr.awildelephant.rdbms.version;

public class TemporaryVersion implements Version {

    private final PermanentVersion databaseVersion;
    private final TransactionId transactionId;

    private Version permanentVersion = EndOfTimesVersion.getInstance();
    private boolean rolledBack;

    public TemporaryVersion(PermanentVersion databaseVersion, TransactionId transactionId) {
        this.databaseVersion = databaseVersion;
        this.transactionId = transactionId;
    }

    @Override
    public boolean isAfter(Version version) {
        return permanentVersion.isAfter(version);
    }

    public PermanentVersion databaseVersion() {
        return databaseVersion;
    }

    public void commit(Version permanentVersion) {
        this.permanentVersion = permanentVersion;
    }

    public void rollBack() {
        rolledBack = true;
    }
}
