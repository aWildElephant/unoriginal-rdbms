package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.table.SetTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.data.tuple.Tuple;

public class DistinctOperator implements Operator {

    @Override
    public Table compute(Table inputTable) {
        final SetTable outputTable = new SetTable(inputTable.schema());

        for (Tuple tuple : inputTable) {
            outputTable.add(tuple);
        }

        return outputTable;
    }
}
