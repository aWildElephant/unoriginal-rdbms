package fr.awildelephant.rdbms.database.version;

import org.jetbrains.annotations.NotNull;

public record PermanentVersion(long value) implements Version, Comparable<PermanentVersion> {

    public static PermanentVersion BEGINNING_OF_TIMES = new PermanentVersion(Long.MIN_VALUE);
    public static PermanentVersion END_OF_TIMES = new PermanentVersion(Long.MAX_VALUE);

    public PermanentVersion next() {
        return new PermanentVersion(value + 1);
    }

    @Override
    public PermanentVersion permanentVersion() {
        return this;
    }

    @Override
    public int compareTo(@NotNull PermanentVersion other) {
        return Long.compare(value, other.value);
    }
}
