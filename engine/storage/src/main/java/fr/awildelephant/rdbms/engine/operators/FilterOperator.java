package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.values.RecordValues;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.schema.Schema;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;

public final class FilterOperator implements Operator<Table, Table> {

    private final Formula filter;

    public FilterOperator(Formula filter) {
        this.filter = filter;
    }

    @Override
    public Table compute(Table inputTable) {
        final Schema inputSchema = inputTable.schema();
        final Table outputTable = simpleTable(inputSchema);

        final RecordValues values = new RecordValues();

        for (Record record : inputTable) {
            values.setRecord(record);

            final DomainValue predicateResult = filter.evaluate(values);

            if (!predicateResult.isNull() && predicateResult.getBool()) {
                outputTable.add(record);
            }
        }

        return outputTable;
    }
}
