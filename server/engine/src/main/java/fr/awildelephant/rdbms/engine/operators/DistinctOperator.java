package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.distinctTable;

public class DistinctOperator implements Operator<Table, Table> {

    @Override
    public Table compute(Table inputTable) {
        final Table outputTable = distinctTable(inputTable.schema());

        for (Record record : inputTable) {
            outputTable.add(record);
        }

        return outputTable;
    }
}
