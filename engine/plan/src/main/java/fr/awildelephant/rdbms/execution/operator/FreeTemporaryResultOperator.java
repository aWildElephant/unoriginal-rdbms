package fr.awildelephant.rdbms.execution.operator;

import fr.awildelephant.rdbms.execution.executor.TemporaryStorage;
import fr.awildelephant.rdbms.storage.data.table.Table;

public record FreeTemporaryResultOperator(Iterable<String> keys) implements Operator {

    @Override
    public Table compute(TemporaryStorage storage) {
        keys.forEach(storage::freeTemporaryResult);

        return null;
    }
}
