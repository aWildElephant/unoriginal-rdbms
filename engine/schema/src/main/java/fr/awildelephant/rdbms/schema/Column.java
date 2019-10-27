package fr.awildelephant.rdbms.schema;

import java.util.Objects;

// TODO: split the index and the rest of the fields
public final class Column {

    private final int index;
    private final ColumnReference name;
    private final Domain domain;
    private final boolean notNull;

    public Column(int index, ColumnReference name, Domain domain, boolean notNull) {
        this.name = name;
        this.domain = domain;
        this.index = index;
        this.notNull = notNull;
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

    @Override
    public int hashCode() {
        return Objects.hash(name, domain, index, notNull);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Column)) {
            return false;
        }

        final Column other = (Column) obj;

        return index == other.index
                && notNull == other.notNull
                && domain == other.domain
                && Objects.equals(name, other.name);
    }
}
