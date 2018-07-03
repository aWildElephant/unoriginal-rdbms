package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.ast.InsertInto;
import fr.awildelephant.rdbms.engine.data.index.UniqueIndex;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.data.value.DecimalValue;
import fr.awildelephant.rdbms.engine.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.value.IntegerValue;
import fr.awildelephant.rdbms.engine.data.value.StringValue;
import fr.awildelephant.rdbms.schema.Column;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.Schema;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.awildelephant.rdbms.engine.data.value.NullValue.NULL_VALUE;
import static java.util.Collections.singletonList;

final class Inserter {

    private Inserter() {

    }


    static void insertRows(InsertInto insertInto, Table table) {
        final Schema schema = table.schema();

        final Column[] columns = new Column[schema.numberOfAttributes()];
        final Map<Integer, UniqueIndex> uniqueIndexes = new HashMap<>();

        for (String columnName : schema.columnNames()) {
            final Column column = schema.column(columnName);

            columns[schema.indexOf(columnName)] = column;

            if (column.unique()) {
                uniqueIndexes.put(column.index(), table.indexOn(column.name()));
            }
        }

        insertInto.rows().rows().forEach(row -> table.add(createTuple(row.values(), columns, uniqueIndexes)));
    }

    private static Record createTuple(List<Object> row, Column[] columns, Map<Integer, UniqueIndex> uniqueIndexes) {
        final DomainValue[] values = new DomainValue[columns.length];

        for (int i = 0; i < row.size(); i++) {
            final Column column = columns[i];
            final Object obj = row.get(i);
            final DomainValue value = wrap(obj, column.domain());

            if (column.notNull() && value.isNull()) {
                throw new IllegalArgumentException("Cannot insert NULL in not-null column " + column.name());
            }

            if (!value.isNull() && uniqueIndexes.containsKey(i) && uniqueIndexes.get(i)
                                                                                .contains(singletonList(value))) {
                throw new IllegalArgumentException("Unique constraint violation: column " + column
                        .name() + " already contains value " + obj);
            }

            values[i] = value;
        }

        return new Record(values);
    }

    private static DomainValue wrap(Object obj, Domain domain) {
        if (obj == null) {
            return NULL_VALUE;
        }

        switch (domain) {
            case DECIMAL:
                return new DecimalValue((BigDecimal) obj);
            case INTEGER:
                return new IntegerValue((int) obj);
            case TEXT:
                return new StringValue((String) obj);
            default:
                throw new IllegalStateException();
        }
    }
}
