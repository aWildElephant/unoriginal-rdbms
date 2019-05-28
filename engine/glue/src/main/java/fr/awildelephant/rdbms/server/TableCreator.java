package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.ast.ColumnDefinition;
import fr.awildelephant.rdbms.ast.CreateTable;
import fr.awildelephant.rdbms.ast.TableElementList;
import fr.awildelephant.rdbms.ast.constraints.NotNullConstraint;
import fr.awildelephant.rdbms.ast.constraints.UniqueConstraint;
import fr.awildelephant.rdbms.engine.constraint.NotNullChecker;
import fr.awildelephant.rdbms.engine.constraint.UniqueChecker;
import fr.awildelephant.rdbms.engine.data.index.UniqueIndex;
import fr.awildelephant.rdbms.engine.data.table.TableWithChecker;
import fr.awildelephant.rdbms.schema.Column;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static fr.awildelephant.rdbms.ast.ColumnDefinition.DATE;
import static fr.awildelephant.rdbms.ast.ColumnDefinition.DECIMAL;
import static fr.awildelephant.rdbms.ast.ColumnDefinition.INTEGER;
import static fr.awildelephant.rdbms.ast.ColumnDefinition.TEXT;
import static fr.awildelephant.rdbms.engine.data.table.TableFactory.tableWithChecker;

final class TableCreator {

    private TableCreator() {

    }

    static TableWithChecker tableFrom(CreateTable createTable) {
        final List<Column> columns = attributesOf(createTable);
        final Schema schema = new Schema(columns);
        final TableWithChecker table = tableWithChecker(schema);

        createConstraintsOn(createTable.tableElementList(), table);

        return table;
    }

    private static void createConstraintsOn(TableElementList elementList, TableWithChecker table) {
        final Schema schema = table.schema();

        for (NotNullConstraint constraint : elementList.notNullConstraints()) {
            final String columnName = constraint.columnName();

            checkColumnExist(columnName, schema);

            table.addChecker(new NotNullChecker(schema.indexOf(columnName), columnName));
        }

        for (UniqueConstraint constraint : elementList.uniqueConstraints()) {
            final Set<String> columnNames = constraint.columnNames();

            columnNames.forEach(columnName -> checkColumnExist(columnName, schema));

            final ArrayList<String> orderedColumnNames = new ArrayList<>(columnNames);

            final UniqueIndex index = table.createIndexOn(orderedColumnNames);

            table.addChecker(new UniqueChecker(index));
        }
    }

    private static void checkColumnExist(String columnName, Schema schema) {
        if (!schema.contains(columnName)) {
            throw new IllegalArgumentException("Column not found: " + columnName);
        }
    }

    private static List<Column> attributesOf(CreateTable createTable) {
        final TableElementList elements = createTable.tableElementList();
        final List<ColumnDefinition> columnDefinitions = elements.columns();
        final ArrayList<Column> columns = new ArrayList<>(columnDefinitions.size());

        int i = 0;
        for (ColumnDefinition element : columnDefinitions) {
            columns.add(new Column(i, element.columnName(), domainOf(element.columnType()), false));
            i = i + 1;
        }

        return columns;
    }

    private static Domain domainOf(int columnType) {
        switch (columnType) {
            case DATE:
                return Domain.DATE;
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
}
