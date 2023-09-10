package fr.awildelephant.rdbms.execution.operator;

import fr.awildelephant.rdbms.execution.executor.TemporaryStorage;
import fr.awildelephant.rdbms.execution.operator.sort.MultipleColumnsComparator;
import fr.awildelephant.rdbms.execution.sort.SortSpecification;
import fr.awildelephant.rdbms.storage.data.record.Record;
import fr.awildelephant.rdbms.storage.data.table.Table;
import fr.awildelephant.rdbms.storage.data.table.TableFactory;
import fr.awildelephant.rdbms.storage.data.table.WriteableTable;

import java.util.ArrayList;
import java.util.List;

public class SortOperator implements Operator {

    private final String inputKey;
    private final List<SortSpecification> sortSpecificationList;

    public SortOperator(String inputKey, List<SortSpecification> sortSpecificationList) {
        this.inputKey = inputKey;
        this.sortSpecificationList = sortSpecificationList;
    }

    @Override
    public Table compute(TemporaryStorage storage) {
        final Table inputTable = storage.get(inputKey);

        final MultipleColumnsComparator comparator = new MultipleColumnsComparator(inputTable.schema(), sortSpecificationList);
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
