package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.data.table.TableFactory;
import fr.awildelephant.rdbms.engine.operators.values.RecordValues;
import fr.awildelephant.rdbms.evaluator.Formula;

import java.util.function.Predicate;

public record FilterOperator(Formula filter) implements Operator<Table, Table> {

    @Override
    public Table compute(Table inputTable) {
        final RecordValues values = new RecordValues();
        final Predicate<Record> predicate = record -> {
            values.setRecord(record);

            final DomainValue predicateResult = filter.evaluate(values);

            return !predicateResult.isNull() && predicateResult.getBool();
        };

        return TableFactory.filter(inputTable, predicate).materialize();
    }
}
