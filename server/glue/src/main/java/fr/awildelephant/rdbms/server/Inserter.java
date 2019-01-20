package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.InsertInto;
import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.schema.Column;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.List;

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

    private static Record createTuple(List<AST> row, Domain[] domains) {
        checkColumnCount(row.size(), domains.length);

        final DomainValue[] values = new DomainValue[domains.length];

        for (int i = 0; i < row.size(); i++) {
            values[i] = wrap(row.get(i), domains[i]);
        }

        return new Record(values);
    }

    private static void checkColumnCount(int actual, int expected) {
        if (actual != expected) {
            throw new IllegalArgumentException("Column count mismatch: expected " + expected + " but got " + actual);
        }
    }

    // TODO: throw error upon inserting an incompatible value
    private static DomainValue wrap(AST ast, Domain domain) {
        return new ObjectWrapper().apply(ast);
    }
}
