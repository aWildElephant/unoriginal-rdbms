package fr.awildelephant.rdbms.schema;

public class Attribute {

    private final String name;
    private final Domain domain;

    public Attribute(String name, Domain domain) {
        this.name = name;
        this.domain = domain;
    }

    public String name() {
        return name;
    }

    public Domain domain() {
        return domain;
    }
}
