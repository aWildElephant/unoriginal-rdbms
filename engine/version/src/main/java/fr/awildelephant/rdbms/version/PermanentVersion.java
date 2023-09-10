package fr.awildelephant.rdbms.version;

public record PermanentVersion(long value) implements Version {

    public PermanentVersion next() {
        return new PermanentVersion(value + 1);
    }

    public boolean isAfter(Version version) {
        if (version instanceof final PermanentVersion other) {
            return value > other.value;
        }

        return false;
    }
}
