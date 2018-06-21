package fr.awildelephant.rdbms.schema;

public class IndexedAttribute extends Attribute {
    private final int index;

    public IndexedAttribute(int index, String name, Domain domain) {
        super(name, domain);
        this.index = index;
    }

    public int index() {
        return index;
    }
}
