package fr.awildelephant.rdbms.schema;

import java.util.Objects;

public final class Column {

    private final int index;
    private final String name;
    private final Domain domain;
    private final boolean notNull;

    public Column(int index, String name, Domain domain, boolean notNull) {
        this.name = name;
        this.domain = domain;
        this.index = index;
        this.notNull = notNull;
    }

    int index() {
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
