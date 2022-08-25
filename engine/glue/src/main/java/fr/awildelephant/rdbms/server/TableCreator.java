package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.ast.ColumnDefinition;
import fr.awildelephant.rdbms.ast.ColumnType;
import fr.awildelephant.rdbms.ast.CreateTable;
import fr.awildelephant.rdbms.ast.TableElementList;
import fr.awildelephant.rdbms.ast.constraints.NotNullConstraint;
import fr.awildelephant.rdbms.ast.constraints.UniqueConstraint;
import fr.awildelephant.rdbms.engine.constraint.NotNullChecker;
import fr.awildelephant.rdbms.engine.constraint.UniqueChecker;
import fr.awildelephant.rdbms.engine.data.index.UniqueIndex;
import fr.awildelephant.rdbms.engine.data.table.TableWithChecker;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.QualifiedColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.tableWithChecker;

final class TableCreator {

    private TableCreator() {

    }

    static TableWithChecker tableFrom(CreateTable createTable) {
        final List<ColumnMetadata> columns = attributesOf(createTable);
        final Schema schema = Schema.of(columns);
        final TableWithChecker table = tableWithChecker(schema);

        createConstraintsOn(createTable.columns(), table);

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
        schema.column(columnName);
    }

    private static List<ColumnMetadata> attributesOf(CreateTable createTable) {
        final String tableName = createTable.tableName().name();
        final TableElementList elements = createTable.columns();
        final List<ColumnDefinition> columnDefinitions = elements.columns();
        final ArrayList<ColumnMetadata> columns = new ArrayList<>(columnDefinitions.size());

        final Set<String> notNullColumns = notNullColumns(createTable.columns().notNullConstraints());

        for (ColumnDefinition element : columnDefinitions) {
            final String columnName = element.columnName();
            final QualifiedColumnReference columnReference = new QualifiedColumnReference(tableName, columnName);
            final Domain columnType = domainOf(element.columnType());
            final boolean notNull = notNullColumns.contains(columnName);
            columns.add(new ColumnMetadata(columnReference, columnType, notNull, false));
        }

        return columns;
    }

    private static Set<String> notNullColumns(Iterable<NotNullConstraint> notNullConstraints) {
        final Set<String> notNullColumns = new HashSet<>();
        for (NotNullConstraint notNullConstraint : notNullConstraints) {
            notNullColumns.add(notNullConstraint.columnName());
        }
        return notNullColumns;
    }

    private static Domain domainOf(ColumnType columnType) {
        return switch (columnType) {
            case BOOLEAN -> Domain.BOOLEAN;
            case BIGINT -> Domain.LONG;
            case DATE -> Domain.DATE;
            case DECIMAL -> Domain.DECIMAL;
            case INTEGER -> Domain.INTEGER;
            case TEXT -> Domain.TEXT;
        };
    }
}
