package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.data.table.TableFactory;
import fr.awildelephant.rdbms.engine.data.table.WriteableTable;
import fr.awildelephant.rdbms.engine.operators.sort.MultipleColumnsComparator;
import fr.awildelephant.rdbms.plan.sort.SortSpecification;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;

public final class SortOperator implements Operator<Table, Table> {

    private final MultipleColumnsComparator comparator;

    public SortOperator(Schema inputSchema, List<SortSpecification> sortSpecificationList) {
        comparator = new MultipleColumnsComparator(inputSchema, sortSpecificationList);
    }

    @Override
    public Table compute(Table inputTable) {
        final List<Record> sortedList = new ArrayList<>(inputTable.numberOfTuples());

        for (Record record : inputTable) {
            sortedList.add(record.materialize());
        }

        sortedList.sort(comparator);

        final WriteableTable outputTable = TableFactory.simpleTable(inputTable.schema(), sortedList.size());
        outputTable.addAll(sortedList);
        return outputTable;
    }
}
