package fr.awildelephant.rdbms.execution.operator;

import fr.awildelephant.rdbms.engine.data.table.AliasedTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.execution.executor.TemporaryStorage;
import fr.awildelephant.rdbms.schema.Schema;

public class AliasOperator implements Operator {

    private final String inputKey;
    private final Schema schema;

    public AliasOperator(String inputKey, Schema schema) {
        this.inputKey = inputKey;
        this.schema = schema;
    }

    @Override
    public Table compute(TemporaryStorage storage) {
        return new AliasedTable(schema, storage.get(inputKey));
    }
}
