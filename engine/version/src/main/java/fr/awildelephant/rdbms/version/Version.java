package fr.awildelephant.rdbms.version;

public interface Version {

    boolean isAfter(Version version);
}
