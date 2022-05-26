package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.data.table.WriteableTable;

import java.util.Iterator;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;

public final class LimitOperator {

    private final int limit;

    public LimitOperator(int limit) {
        this.limit = limit;
    }

    public Table compute(Table input) {
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
