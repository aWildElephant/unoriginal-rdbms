package fr.awildelephant.rdbms.execution.operator;

import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.execution.executor.TemporaryStorage;

public record FreeTemporaryResultOperator(Iterable<String> keys) implements Operator {

    @Override
    public Table compute(TemporaryStorage storage) {
        keys.forEach(storage::freeTemporaryResult);

        return null;
    }
}
