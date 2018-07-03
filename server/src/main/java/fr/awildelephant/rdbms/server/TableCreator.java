package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.ast.ColumnDefinition;
import fr.awildelephant.rdbms.ast.CreateTable;
import fr.awildelephant.rdbms.ast.TableElementList;
import fr.awildelephant.rdbms.ast.constraints.NotNullConstraint;
import fr.awildelephant.rdbms.ast.constraints.UniqueConstraint;
import fr.awildelephant.rdbms.schema.Column;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.ColumnDefinition.*;

final class TableCreator {

    private TableCreator() {

    }


    static List<Column> attributesOf(CreateTable createTable) {
        final TableElementList elements = createTable.tableElementList();
        final List<ColumnDefinition> columnDefinitions = elements.columns();
        final ArrayList<Column> columns = new ArrayList<>(columnDefinitions.size());

        int i = 0;
        for (ColumnDefinition element : columnDefinitions) {
            columns.add(new Column(i, element.columnName(), domainOf(element.columnType()), false, false));
            i = i + 1;
        }

        for (UniqueConstraint constraint : elements.uniqueConstraints()) {
            updateAndSetUnique(constraint.columnName(), columns);
        }

        for (NotNullConstraint constraint : elements.notNullConstraints()) {
            updateAndSetNotNull(constraint.columnName(), columns);
        }

        return columns;
    }

    private static void updateAndSetNotNull(String columnName, ArrayList<Column> columns) {
        for (int i = 0; i < columns.size(); i++) {
            final Column column = columns.get(i);

            if (column.name().equals(columnName)) {
                columns.set(i, column.butNotNull());
                return;
            }
        }
    }

    private static void updateAndSetUnique(String columnName, ArrayList<Column> columns) {
        for (int i = 0; i < columns.size(); i++) {
            final Column column = columns.get(i);

            if (column.name().equals(columnName)) {
                columns.set(i, column.butUnique());
                return;
            }
        }

        throw new IllegalArgumentException("Column not found: " + columnName);
    }

    private static Domain domainOf(int columnType) {
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
}
