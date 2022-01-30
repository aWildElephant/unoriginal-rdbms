package fr.awildelephant.rdbms.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public final class Schema {

    public static final Schema EMPTY_SCHEMA = new Schema(List.of());

    private final List<ColumnReference> allColumns;

    private final Map<String, Map<String, OrderedColumnMetadata>> columnIndex;

    private Schema(List<ColumnMetadata> columns) {
        final int numberOfColumns = columns.size();
        final List<OrderedColumnMetadata> reindexedColumns = new ArrayList<>(numberOfColumns);

        for (int i = 0; i < numberOfColumns; i++) {
            final ColumnMetadata column = columns.get(i);
            reindexedColumns.add(new OrderedColumnMetadata(i, column));
        }

        columnIndex = new HashMap<>();
        allColumns = new ArrayList<>();

        for (OrderedColumnMetadata column : reindexedColumns) {
            final ColumnReference columnReference = column.metadata().name();
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

    public static Schema empty() {
        return EMPTY_SCHEMA;
    }

    public static Schema of(ColumnMetadata column) {
        return of(List.of(column));
    }

    public static Schema of(List<ColumnMetadata> columns) {
        return new Schema(columns);
    }

    public List<ColumnReference> columnNames() {
        return allColumns;
    }

    public List<ColumnMetadata> columnMetadataList() {
        return columnNames().stream()
                .map(this::column)
                .map(OrderedColumnMetadata::metadata)
                .toList();
    }

    public OrderedColumnMetadata column(String name) {
        final Map<String, OrderedColumnMetadata> tables = columnIndex.get(name);

        if (tables == null || tables.isEmpty()) {
            throw new ColumnNotFoundException(name);
        }

        if (tables.size() > 1) {
            throw new AmbiguousColumnNameException(name);
        }

        return tables.values().iterator().next();
    }

    public OrderedColumnMetadata column(String tableName, String columnName) {
        final Map<String, OrderedColumnMetadata> tables = columnIndex.get(columnName);

        if (tables == null || tables.isEmpty()) {
            throw new ColumnNotFoundException(tableName, columnName);
        }

        final OrderedColumnMetadata column = tables.get(tableName);

        if (column == null) {
            throw new ColumnNotFoundException(tableName, columnName);
        }

        return column;
    }

    public OrderedColumnMetadata column(ColumnReference reference) {
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
        return column(reference).metadata().name();
    }

    public int numberOfAttributes() {
        return allColumns.size();
    }

    public Schema project(Iterable<ColumnReference> references) {
        final List<ColumnMetadata> projection = new ArrayList<>();

        for (ColumnReference reference : references) {
            final ColumnMetadata column = reference.table()
                    .map(qualifier -> column(qualifier, reference.name()))
                    .orElseGet(() -> column(reference.name()))
                    .metadata();

            if (column == null) {
                throw new IllegalStateException(
                        "Column `" + reference + "` not found. Available columns " + this.allColumns);
            }

            projection.add(new ColumnMetadata(column.name(), column.domain(), column.notNull(), column.system()));
        }

        return new Schema(projection);
    }

    public Schema alias(Function<ColumnReference, ColumnReference> alias) {
        final List<ColumnMetadata> columns = allColumns.stream()
                .map(this::column)
                .map(OrderedColumnMetadata::metadata)
                .map(column -> alias(alias, column))
                .toList();

        return new Schema(columns);
    }

    private ColumnMetadata alias(Function<ColumnReference, ColumnReference> alias, ColumnMetadata column) {
        return new ColumnMetadata(alias.apply(column.name()), column.domain(), column.notNull(), column.system());
    }

    public Schema extend(List<ColumnMetadata> newColumns) {
        final ArrayList<ColumnMetadata> combinedColumns = new ArrayList<>(allColumns.size() + newColumns.size());

        for (ColumnReference columnReference : allColumns) {
            combinedColumns.add(column(columnReference).metadata());
        }

        combinedColumns.addAll(newColumns);

        return new Schema(combinedColumns);
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
