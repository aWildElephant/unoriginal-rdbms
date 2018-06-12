package fr.awildelephant.rdbms.engine;

import java.util.Map;

public class Schema {

    private final Map<String, Integer> attributes;

    public Schema(Map<String, Integer> attributes) {
        this.attributes = attributes;
    }

    public int indexOf(String attributeName) {
        return attributes.get(attributeName);
    }
}
