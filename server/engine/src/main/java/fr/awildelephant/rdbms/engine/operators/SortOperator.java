package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.CollectionTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.sort.MultipleColumnsComparator;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * Insertion sort implementation.
 */
public class SortOperator implements Operator<Table, Table> {

    private final MultipleColumnsComparator comparator;

    public SortOperator(Schema inputSchema, List<ColumnName> columns) {
        comparator = new MultipleColumnsComparator(inputSchema, columns);
    }

    @Override
    public Table compute(Table inputTable) {
        final List<Record> sortedList = new ArrayList<>(inputTable.numberOfTuples());

        for (Record record : inputTable) {
            insertInCorrectPosition(record, sortedList);
        }

        return new CollectionTable(inputTable.schema(), sortedList);
    }

    private void insertInCorrectPosition(Record record, List<Record> sortedList) {
        for (int i = 0; i < sortedList.size(); i++) {
            if (comparator.compare(record, sortedList.get(i)) < 0) {
                sortedList.add(i, record);
                return;
            }
        }

        sortedList.add(record);
    }
}
