package fr.awildelephant.rdbms.server;

public final class ReservedKeywords {

    private ReservedKeywords() {

    }

    static final String FROM_VERSION_COLUMN = "<from>";
    static final String TO_VERSION_COLUMN = "<to>";
    static final String SYSTEM_TABLE = "<mvcc>";
}
