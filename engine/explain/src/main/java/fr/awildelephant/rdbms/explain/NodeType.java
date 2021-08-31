package fr.awildelephant.rdbms.explain;

public enum NodeType {
    SUBSTRING;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
