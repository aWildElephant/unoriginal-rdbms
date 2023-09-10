package fr.awildelephant.rdbms.data.value;

import fr.awildelephant.rdbms.version.Version;

public record VersionValue(Version version) implements DomainValue {

    public static VersionValue versionValue(Version version) {
        return new VersionValue(version);
    }

    @Override
    public Version getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "VersionValue[" + version + ']';
    }
}
