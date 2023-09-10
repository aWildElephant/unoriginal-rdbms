package fr.awildelephant.rdbms.execution.operator;

import fr.awildelephant.rdbms.execution.executor.TemporaryStorage;
import fr.awildelephant.rdbms.storage.data.record.Record;
import fr.awildelephant.rdbms.storage.data.table.Table;
import fr.awildelephant.rdbms.storage.data.table.WriteableTable;

import java.util.Iterator;

import static fr.awildelephant.rdbms.storage.data.table.TableFactory.simpleTable;

public class LimitOperator implements Operator {

    private final String inputKey;
    private final int limit;

    public LimitOperator(String inputKey, int limit) {
        this.inputKey = inputKey;
        this.limit = limit;
    }

    @Override
    public Table compute(TemporaryStorage storage) {
        final Table input = storage.get(inputKey);

        final int outputSize = Math.min(limit, input.numberOfTuples());
        final WriteableTable outputTable = simpleTable(input.schema(), outputSize);

        final Iterator<Record> inputIterator = input.iterator();

        int count = 0;

        while (inputIterator.hasNext() && count < limit) {
            outputTable.add(inputIterator.next());
            count++;
        }

        return outputTable;
    }
}
