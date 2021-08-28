package fr.awildelephant.rdbms.schema;

import java.util.Objects;

// TODO: split the index and the rest of the fields
public final class ColumnMetadata {

    private final int index;
    private final ColumnReference name;
    private final Domain domain;
    private final boolean notNull;
    private final boolean system;

    public ColumnMetadata(int index, ColumnReference name, Domain domain, boolean notNull, boolean system) {
        this.name = name;
        this.domain = domain;
        this.index = index;
        this.notNull = notNull;
        this.system = system;
    }

    /**
     * The index of the column in its table, starting from 0
     */
    public int index() {
        return index;
    }

    public ColumnReference name() {
        return name;
    }

    public Domain domain() {
        return domain;
    }

    public boolean notNull() {
        return notNull;
    }

    public boolean system() {
        return system;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, domain, index, notNull, system);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final ColumnMetadata other)) {
            return false;
        }

        return index == other.index
                && notNull == other.notNull
                && domain == other.domain
                && system == other.system
                && Objects.equals(name, other.name);
    }
}
