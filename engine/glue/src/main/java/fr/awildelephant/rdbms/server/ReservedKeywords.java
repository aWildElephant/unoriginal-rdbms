package fr.awildelephant.rdbms.server;

public final class ReservedKeywords {

    private ReservedKeywords() {

    }

    public static final String FROM_VERSION_COLUMN = "<from>";
    public static final String TO_VERSION_COLUMN = "<to>";
    static final String SYSTEM_TABLE = "<mvcc>";
}
