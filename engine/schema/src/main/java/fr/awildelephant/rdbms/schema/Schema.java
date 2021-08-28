package fr.awildelephant.rdbms.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public final class Schema {

    public static final Schema EMPTY_SCHEMA = new Schema(emptyList());

    private final List<ColumnReference> allColumns;

    private final Map<String, Map<String, ColumnMetadata>> columnIndex;

    public Schema(List<ColumnMetadata> columns) {
        final int numberOfColumns = columns.size();
        final List<ColumnMetadata> reindexedColumns = new ArrayList<>(numberOfColumns);

        for (int i = 0; i < numberOfColumns; i++) {
            final ColumnMetadata column = columns.get(i);
            reindexedColumns.add(new ColumnMetadata(i, column.name(), column.domain(), column.notNull(), column.system()));
        }

        columnIndex = new HashMap<>();
        allColumns = new ArrayList<>();

        for (ColumnMetadata column : reindexedColumns) {
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

    public static Schema emptySchema() {
        return EMPTY_SCHEMA;
    }

    public List<ColumnReference> columnNames() {
        return allColumns;
    }

    public ColumnMetadata column(String name) {
        final Map<String, ColumnMetadata> tables = columnIndex.get(name);

        if (tables == null || tables.isEmpty()) {
            throw new ColumnNotFoundException(name);
        }

        if (tables.size() > 1) {
            throw new AmbiguousColumnNameException(name);
        }

        return tables.values().iterator().next();
    }

    public ColumnMetadata column(String tableName, String columnName) {
        final Map<String, ColumnMetadata> tables = columnIndex.get(columnName);

        if (tables == null || tables.isEmpty()) {
            throw new ColumnNotFoundException(tableName, columnName);
        }

        final ColumnMetadata column = tables.get(tableName);

        if (column == null) {
            throw new ColumnNotFoundException(tableName, columnName);
        }

        return column;
    }

    public ColumnMetadata column(ColumnReference reference) {
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

    public ColumnReference normalize(ColumnReference reference) {
        return column(reference).name();
    }

    public int numberOfAttributes() {
        return allColumns.size();
    }

    public Schema project(Iterable<ColumnReference> references) {
        final List<ColumnMetadata> projection = new ArrayList<>();

        int i = 0;
        for (ColumnReference reference : references) {
            final ColumnMetadata column = reference.table()
                                           .map(qualifier -> column(qualifier, reference.name()))
                                           .orElseGet(() -> column(reference.name()));

            if (column == null) {
                throw new IllegalStateException(
                        "Column `" + reference + "` not found. Available columns " + this.allColumns);
            }

            projection.add(new ColumnMetadata(i, column.name(), column.domain(), column.notNull(), column.system()));
            i = i + 1;
        }

        return new Schema(projection);
    }

    public Schema alias(Function<ColumnReference, ColumnReference> alias) {
        final List<ColumnMetadata> columns = allColumns.stream()
                                               .map(this::column)
                                               .map(column -> new ColumnMetadata(column.index(),
                                                                         alias.apply(column.name()),
                                                                         column.domain(),
                                                                         column.notNull(),
                                                                         column.system()))
                                               .collect(toList());

        return new Schema(columns);
    }

    public Schema extend(List<ColumnMetadata> newColumns) {
        final ArrayList<ColumnMetadata> allColumns = new ArrayList<>(this.allColumns.size() + newColumns.size());

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
        if (!(obj instanceof final Schema other)) {
            return false;
        }

        return Objects.equals(allColumns, other.allColumns)
                && Objects.equals(columnIndex, other.columnIndex);
    }
}
