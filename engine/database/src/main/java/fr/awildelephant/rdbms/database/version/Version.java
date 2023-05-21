package fr.awildelephant.rdbms.database.version;

public interface Version {

    boolean isAfter(Version version);
}
