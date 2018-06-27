package fr.awildelephant.rdbms.schema;

import java.util.Objects;

public final class Column {

    private final int index;
    private final String name;
    private final Domain domain;

    public Column(int index, String name, Domain domain) {
        this.name = name;
        this.domain = domain;
        this.index = index;
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

    @Override
    public int hashCode() {
        return index * 32 + Objects.hash(name, domain);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Column)) {
            return false;
        }

        final Column other = (Column) obj;

        return index == other.index
                && domain == other.domain
                && Objects.equals(name, other.name);
    }
}
