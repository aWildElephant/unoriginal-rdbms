package fr.awildelephant.rdbms.schema;

import java.util.Map;
import java.util.Set;

public class Schema {

    private final Map<String, Integer> attributes;

    public Schema(Map<String, Integer> attributes) {
        this.attributes = attributes;
    }

    public Set<String> attributes() {
        return attributes.keySet();
    }

    public int indexOf(String attributeName) {
        return attributes.get(attributeName);
    }

    public boolean contains(String attributeName) {
        return attributes.containsKey(attributeName);
    }
}
