package fr.awildelephant.rdbms.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public final class Schema {

    private final List<String> columnNames;
    private final Map<String, Column> columnIndex;

    public Schema(List<Column> columns) {
        final int numberOfColumns = columns.size();
        final List<Column> reindexedColumns = new ArrayList<>(numberOfColumns);

        for (int i = 0; i < numberOfColumns; i++) {
            final Column column = columns.get(i);
            reindexedColumns.add(new Column(i, column.name(), column.domain(), column.notNull()));
        }

        final Map<String, Column> indexedAttributes = new HashMap<>(numberOfColumns);
        final String[] nameList = new String[numberOfColumns];

        for (Column column : reindexedColumns) {
            nameList[column.index()] = column.name();
            indexedAttributes.put(column.name(), column);
        }

        columnNames = List.of(nameList);
        columnIndex = indexedAttributes;
    }

    private Schema(List<String> columnNames, Map<String, Column> columnIndex) {
        this.columnNames = columnNames;
        this.columnIndex = columnIndex;
    }

    public List<String> columnNames() {
        return columnNames;
    }

    public Column column(String attributeName) {
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

            if (column == null) {
                throw new IllegalStateException("Column `" + name + "` not found. Available columns " + columnNames);
            }

            newIndex.put(name, new Column(i, column.name(), column.domain(), column.notNull()));
            i = i + 1;
        }

        return new Schema(names, newIndex);
    }

    public Schema alias(Alias alias) {
        final Map<String, Column> newColumnIndex = columnIndex.entrySet()
                                                              .stream()
                                                              .collect(toMap(entry -> alias.get(entry.getKey()), Map.Entry::getValue));

        final List<String> newColumnNames = columnNames.stream()
                                                       .map(alias::get)
                                                       .collect(toList());

        return new Schema(newColumnNames, newColumnIndex);
    }

    public Schema extend(List<Column> newColumns) {
        final ArrayList<Column> allColumns = new ArrayList<>(columnNames.size() + newColumns.size());

        for (String columnName : columnNames) {
            allColumns.add(column(columnName));
        }

        allColumns.addAll(newColumns);

        return new Schema(allColumns);
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
