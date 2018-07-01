package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.algebraizer.Algebraizer;
import fr.awildelephant.rdbms.ast.*;
import fr.awildelephant.rdbms.engine.Engine;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.awildelephant.rdbms.ast.ColumnDefinition.*;
import static fr.awildelephant.rdbms.engine.data.value.NullValue.NULL_VALUE;
import static java.util.Collections.singletonList;

public class QueryDispatcher extends DefaultASTVisitor<Table> {

    private final Engine engine;
    private final Algebraizer algebraizer;

    QueryDispatcher(final Engine engine, final Algebraizer algebraizer) {
        this.engine = engine;
        this.algebraizer = algebraizer;
    }

    @Override
    public Table visit(CreateTable createTable) {
        final String tableName = createTable.tableName().name();

        checkTableDoesNotExist(tableName);

        final List<Column> columns = attributesOf(createTable);

        engine.create(tableName, columns);

        return null;
    }

    @Override
    public Table visit(DropTable dropTable) {
        final String tableName = dropTable.tableName().name();

        engine.drop(tableName);

        return null;
    }

    @Override
    public Table visit(InsertInto insertInto) {
        final String tableName = insertInto.targetTable().name();
        final Table table = engine.get(tableName);
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

        return null;
    }

    @Override
    public Table visit(Select select) {
        return engine.execute(algebraizer.apply(select));
    }

    @Override
    public Table defaultVisit(AST node) {
        throw new IllegalStateException();
    }

    private List<Column> attributesOf(CreateTable createTable) {
        final List<ColumnDefinition> elements = createTable.tableElementList().elements();
        final ArrayList<Column> columns = new ArrayList<>(elements.size());

        int i = 0;
        for (ColumnDefinition element : elements) {
            columns.add(new Column(i, element.columnName(), domainOf(element.columnType()), element.notNull(), element.unique()));
            i = i + 1;
        }

        return columns;
    }

    private void checkTableDoesNotExist(String tableName) {
        if (engine.exists(tableName)) {
            throw new IllegalArgumentException("Table " + tableName + " already exists");
        }
    }

    private Domain domainOf(int columnType) {
        switch (columnType) {
            case DECIMAL:
                return Domain.DECIMAL;
            case INTEGER:
                return Domain.INTEGER;
            case TEXT:
                return Domain.TEXT;
            default:
                throw new IllegalArgumentException();
        }
    }

    private Record createTuple(List<Object> row, Column[] columns, Map<Integer, UniqueIndex> uniqueIndexes) {
        final DomainValue[] values = new DomainValue[columns.length];

        for (int i = 0; i < row.size(); i++) {
            final Column column = columns[i];
            final Object obj = row.get(i);
            final DomainValue value = wrap(obj, column.domain());

            if (column.notNull() && value.isNull()) {
                throw new IllegalArgumentException("Cannot insert NULL in not-null column " + column.name());
            }

            if (!value.isNull() && uniqueIndexes.containsKey(i) && uniqueIndexes.get(i).contains(singletonList(value))) {
                throw new IllegalArgumentException("Unique constraint violation: column " + column.name() + " already contains value " + obj);
            }

            values[i] = value;
        }

        return new Record(values);
    }

    private DomainValue wrap(Object obj, Domain domain) {
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
