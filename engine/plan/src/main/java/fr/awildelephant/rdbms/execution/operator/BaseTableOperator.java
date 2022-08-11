package fr.awildelephant.rdbms.execution.operator;

import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.execution.executor.TemporaryStorage;

public class BaseTableOperator implements Operator {

    private final String tableName;

    public BaseTableOperator(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public Table compute(TemporaryStorage storage) {
        return storage.get(tableName);
    }
}
