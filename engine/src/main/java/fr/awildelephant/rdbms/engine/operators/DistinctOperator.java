package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.SetTable;
import fr.awildelephant.rdbms.engine.data.table.Table;

public class DistinctOperator implements Operator {

    @Override
    public Table compute(Table inputTable) {
        final SetTable outputTable = new SetTable(inputTable.schema());

        for (Record record : inputTable) {
            outputTable.add(record);
        }

        return outputTable;
    }
}
