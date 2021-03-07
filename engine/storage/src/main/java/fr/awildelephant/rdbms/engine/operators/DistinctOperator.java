package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;

import java.util.List;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.distinctTable;

public class DistinctOperator implements Operator<Table, Table> {

    @Override
    public Table compute(Table inputTable) {
        final AggregationOperator fakeOperator = new AggregationOperator(List.of(),
                                                                                inputTable.schema().columnNames(),
                                                                                inputTable.schema());

        return fakeOperator.compute(inputTable);
    }
}
