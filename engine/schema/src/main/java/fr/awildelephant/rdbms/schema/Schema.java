package fr.awildelephant.rdbms.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public final class Schema {

    private final List<ColumnReference> allColumns;
    private final Map<String, Map<String, Column>> columnIndex;

    public Schema(List<Column> columns) {
        final int numberOfColumns = columns.size();
        final List<Column> reindexedColumns = new ArrayList<>(numberOfColumns);

        for (int i = 0; i < numberOfColumns; i++) {
            final Column column = columns.get(i);
            reindexedColumns.add(new Column(i, column.name(), column.domain(), column.notNull(), column.system()));
        }

        columnIndex = new HashMap<>();
        allColumns = new ArrayList<>();

        for (Column column : reindexedColumns) {
            final ColumnReference columnReference = column.name();
            allColumns.add(columnReference);
            columnIndex.compute(columnReference.name(), (unused, tables) -> {
                if (tables == null) {
                    tables = new HashMap<>();
                }

                tables.put(columnReference.table().orElse(""), column);

                return tables;
            });
        }
    }

    public List<ColumnReference> columnNames() {
        return allColumns;
    }

    public Column column(String name) {
        final Map<String, Column> tables = columnIndex.get(name);

        if (tables == null || tables.isEmpty()) {
            throw new ColumnNotFoundException(name);
        }

        if (tables.size() > 1) {
            throw new AmbiguousColumnNameException(name);
        }

        return tables.values().iterator().next();
    }

    public Column column(String tableName, String columnName) {
        final Map<String, Column> tables = columnIndex.get(columnName);

        if (tables == null || tables.isEmpty()) {
            throw new ColumnNotFoundException(tableName, columnName);
        }

        final Column column = tables.get(tableName);

        if (column == null) {
            throw new ColumnNotFoundException(tableName, columnName);
        }

        return column;
    }

    public Column column(ColumnReference reference) {
        return reference.table()
                        .map(qualifier -> column(qualifier, reference.name()))
                        .orElseGet(() -> column(reference.name()));
    }

    public boolean contains(ColumnReference columnReference) {
        try {
            column(columnReference);
            return true;
        } catch (ColumnNotFoundException unused) {
            return false;
        }
    }

    public int indexOf(String attributeName) {
        return column(attributeName).index();
    }

    public int indexOf(ColumnReference columnReference) {
        return column(columnReference).index();
    }

    public int numberOfAttributes() {
        return allColumns.size();
    }

    public Schema project(List<ColumnReference> references) {
        final List<Column> projection = new ArrayList<>();

        int i = 0;
        for (ColumnReference reference : references) {
            final Column column = reference.table()
                                           .map(qualifier -> column(qualifier, reference.name()))
                                           .orElseGet(() -> column(reference.name()));

            if (column == null) {
                throw new IllegalStateException(
                        "Column `" + reference + "` not found. Available columns " + this.allColumns);
            }

            projection.add(new Column(i, column.name(), column.domain(), column.notNull(), column.system()));
            i = i + 1;
        }

        return new Schema(projection);
    }

    public Schema alias(Function<ColumnReference, ColumnReference> alias) {
        final List<Column> columns = allColumns.stream()
                                               .map(this::column)
                                               .map(column -> new Column(column.index(),
                                                                         alias.apply(column.name()),
                                                                         column.domain(),
                                                                         column.notNull(),
                                                                         column.system()))
                                               .collect(toList());

        return new Schema(columns);
    }

    public Schema extend(List<Column> newColumns) {
        final ArrayList<Column> allColumns = new ArrayList<>(this.allColumns.size() + newColumns.size());

        for (ColumnReference columnReference : this.allColumns) {
            allColumns.add(column(columnReference));
        }

        allColumns.addAll(newColumns);

        return new Schema(allColumns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnIndex, allColumns);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Schema)) {
            return false;
        }

        final Schema other = (Schema) obj;

        return Objects.equals(allColumns, other.allColumns)
                && Objects.equals(columnIndex, other.columnIndex);
    }
}
