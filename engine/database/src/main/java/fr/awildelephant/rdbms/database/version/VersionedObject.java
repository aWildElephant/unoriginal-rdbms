package fr.awildelephant.rdbms.database.version;

public final class VersionedObject<T> {

    private final T object;
    private final Version fromVersion;
    private final Version toVersion;

    private VersionedObject(T object, Version fromVersion, Version toVersion) {
        this.object = object;
        this.fromVersion = fromVersion;
        this.toVersion = toVersion;
    }

    public static <T> VersionedObject<T> from(T object, Version version) {
        return new VersionedObject<>(object, version, EndOfTimesVersion.getInstance());
    }

    public T object() {
        return object;
    }

    public VersionedObject<T> endAt(Version version) {
        return new VersionedObject<>(object, fromVersion, version);
    }

    public boolean isValidAt(Version version) {
        return !fromVersion.isAfter(version) && toVersion.isAfter(version);
    }
}
