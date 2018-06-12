package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.ast.*;
import fr.awildelephant.rdbms.engine.Engine;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class QueryDispatcher extends DefaultASTVisitor<Void> {

    private final Engine engine;

    QueryDispatcher(final Engine engine) {
        this.engine = engine;
    }

    @Override
    public Void visit(CreateTable createTable) {
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
    public Void visit(InsertInto insertInto) {
        final String tableName = insertInto.targetTable().name();
        final List<List<Integer>> values = insertInto
                .rows()
                .rows()
                .stream()
                .map(Row::values)
                .collect(toList());

        engine.insert(tableName, values);

        return null;
    }

    @Override
    public Void visit(Select select) {
        return null;
    }

    @Override
    public Void defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}
