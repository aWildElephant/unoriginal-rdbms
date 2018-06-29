package fr.awildelephant.rdbms.schema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

public final class Schema {

    private final List<String> columnNames;
    private final Map<String, Column> columnIndex;

    private Schema(List<String> columnNames, Map<String, Column> columnIndex) {
        this.columnNames = columnNames;
        this.columnIndex = columnIndex;
    }

    public Schema(List<Column> columns) {
        final int numberOfAttributes = columns.size();

        final Map<String, Column> indexedAttributes = new HashMap<>(numberOfAttributes);
        final String[] nameList = new String[numberOfAttributes];

        for (Column column : columns) {
            nameList[column.index()] = column.name();
            indexedAttributes.put(column.name(), column);
        }

        columnNames = unmodifiableList(asList(nameList));
        columnIndex = indexedAttributes;
    }

    public List<String> columnNames() {
        return columnNames;
    }

    public Column attribute(String attributeName) {
        return columnIndex.get(attributeName);
    }

    public boolean contains(String attributeName) {
        return columnIndex.containsKey(attributeName);
    }

    public int indexOf(String attributeName) {
        return columnIndex.get(attributeName).index();
    }

    public int numberOfAttributes() {
        return columnIndex.size();
    }

    public Schema project(List<String> names) {
        final HashMap<String, Column> newIndex = new HashMap<>(names.size());

        int i = 0;
        for (String name : names) {
            final Column column = columnIndex.get(name);

            newIndex.put(name, new Column(i, column.name(), column.domain(), column.notNull()));
            i = i + 1;
        }

        return new Schema(names, newIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnIndex, columnNames);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Schema)) {
            return false;
        }

        final Schema other = (Schema) obj;

        return Objects.equals(columnNames, other.columnNames)
                && Objects.equals(columnIndex, other.columnIndex);
    }
}
