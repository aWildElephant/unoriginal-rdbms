package fr.awildelephant.rdbms.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Schema {

    private final List<String> attributeNames;
    private final Map<String, IndexedAttribute> index;

    private Schema(List<String> attributeNames, Map<String, IndexedAttribute> index) {
        this.attributeNames = attributeNames;
        this.index = index;
    }

    public Schema(List<Attribute> orderedAttributes) {
        final int numberOfAttributes = orderedAttributes.size();

        final Map<String, IndexedAttribute> indexedAttributes = new HashMap<>(numberOfAttributes);
        final List<String> nameList = new ArrayList<>(numberOfAttributes);

        int i = 0;
        for (Attribute attribute : orderedAttributes) {
            nameList.add(attribute.name());
            indexedAttributes.put(attribute.name(), new IndexedAttribute(i, attribute.name(), attribute.domain()));
            i = i + 1;
        }

        attributeNames = nameList;
        index = indexedAttributes;
    }

    public List<String> attributeNames() {
        return attributeNames;
    }

    public Attribute attribute(String attributeName) {
        return index.get(attributeName);
    }

    public boolean contains(String attributeName) {
        return index.containsKey(attributeName);
    }

    public int indexOf(String attributeName) {
        return index.get(attributeName).index();
    }

    public int numberOfAttributes() {
        return index.size();
    }

    public Schema project(List<String> names) {
        final HashMap<String, IndexedAttribute> newIndex = new HashMap<>(names.size());

        int i = 0;
        for (String name : names) {
            final IndexedAttribute attribute = index.get(name);

            newIndex.put(name, new IndexedAttribute(i, attribute.name(), attribute.domain()));
            i = i + 1;
        }

        return new Schema(names, newIndex);
    }
}
