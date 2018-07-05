package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.ast.InsertInto;
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
import java.util.List;

import static fr.awildelephant.rdbms.engine.data.value.NullValue.NULL_VALUE;

final class Inserter {

    private Inserter() {

    }

    static void insertRows(InsertInto insertInto, Table table) {
        final Schema schema = table.schema();

        final Domain[] domains = new Domain[schema.numberOfAttributes()];

        for (String columnName : schema.columnNames()) {
            final Column column = schema.column(columnName);

            domains[schema.indexOf(columnName)] = column.domain();
        }

        insertInto.rows().rows().forEach(row -> table.add(createTuple(row.values(), domains)));
    }

    private static Record createTuple(List<Object> row, Domain[] domains) {
        final DomainValue[] values = new DomainValue[domains.length];

        for (int i = 0; i < row.size(); i++) {
            values[i] = wrap(row.get(i), domains[i]);
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
