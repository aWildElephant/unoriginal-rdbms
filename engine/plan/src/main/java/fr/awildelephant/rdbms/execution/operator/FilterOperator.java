package fr.awildelephant.rdbms.execution.operator;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.data.table.TableFactory;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.execution.executor.TemporaryStorage;
import fr.awildelephant.rdbms.execution.operator.values.RecordValues;

import java.util.function.Predicate;

public record FilterOperator(String inputKey, Formula filter) implements Operator {

    @Override
    public Table compute(TemporaryStorage storage) {
        final Table inputTable = storage.get(inputKey);

        final RecordValues values = new RecordValues();
        final Predicate<Record> predicate = record -> {
            values.setRecord(record);

            final DomainValue predicateResult = filter.evaluate(values);

            return !predicateResult.isNull() && predicateResult.getBool();
        };

        return TableFactory.filter(inputTable, predicate).materialize();
    }
}
