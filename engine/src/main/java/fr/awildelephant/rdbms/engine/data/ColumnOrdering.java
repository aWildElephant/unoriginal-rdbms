package fr.awildelephant.rdbms.engine.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColumnOrdering {

    private final Map<String, Integer> columnMap;

    private ColumnOrdering(Map<String, Integer> columnMap) {
        this.columnMap = columnMap;
    }

    public static ColumnOrdering ordering(List<String> orderedNames) {
        final HashMap<String, Integer> ordering = new HashMap<>(orderedNames.size());

        for (int i = 0; i < orderedNames.size(); i++) {
            ordering.put(orderedNames.get(i), i);
        }

        return new ColumnOrdering(ordering);
    }

    public static ColumnOrdering arbitraryOrdering(Collection<String> names) {
        final HashMap<String, Integer> ordering = new HashMap<>(names.size());

        int i = 0;
        for (String name : names) {
            ordering.put(name, i);

            i++;
        }

        return new ColumnOrdering(ordering);
    }

    public int indexOf(String column) {
        return columnMap.get(column);
    }
}
