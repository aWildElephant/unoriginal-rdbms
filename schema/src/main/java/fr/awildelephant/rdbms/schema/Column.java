package fr.awildelephant.rdbms.schema;

import java.util.Objects;

public final class Column {

    private final int index;
    private final String name;
    private final Domain domain;
    private final boolean notNull;
    private final boolean unique;

    public Column(int index, String name, Domain domain, boolean notNull, boolean unique) {
        this.name = name;
        this.domain = domain;
        this.index = index;
        this.notNull = notNull;
        this.unique = unique;
    }

    public int index() {
        return index;
    }

    public String name() {
        return name;
    }

    public Domain domain() {
        return domain;
    }

    public boolean notNull() {
        return notNull;
    }

    public boolean unique() {
        return unique;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, domain, index, notNull, unique);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Column)) {
            return false;
        }

        final Column other = (Column) obj;

        return index == other.index
                && notNull == other.notNull
                && unique == other.unique
                && domain == other.domain
                && Objects.equals(name, other.name);
    }
}
