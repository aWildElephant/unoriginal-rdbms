package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.algebraizer.Algebraizer;
import fr.awildelephant.rdbms.ast.*;
import fr.awildelephant.rdbms.engine.Engine;
import fr.awildelephant.rdbms.engine.Table;
import fr.awildelephant.rdbms.engine.domain.DomainValue;
import fr.awildelephant.rdbms.engine.domain.IntegerValue;

import java.util.List;

import static java.util.stream.Collectors.toList;

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
        final List<String> columnNames = createTable
                .tableElementList()
                .elements()
                .stream()
                .map(ColumnDefinition::columnName)
                .collect(toList());

        engine.createTable(tableName, columnNames);

        return null;
    }

    @Override
    public Table visit(InsertInto insertInto) {
        final String tableName = insertInto.targetTable().name();
        final List<DomainValue[]> values = insertInto
                .rows()
                .rows()
                .stream()
                .map(row -> row.values().stream().map(IntegerValue::new).toArray(DomainValue[]::new))
                .collect(toList());

        engine.insert(tableName, values);

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
}
