package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.table.AliasedTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.schema.Schema;

public final class AliasOperator implements Operator<Table, Table> {

    private final Schema schema;

    public AliasOperator(Schema schema) {
        this.schema = schema;
    }

    @Override
    public Table compute(Table inputTable) {
        return new AliasedTable(schema, inputTable);
    }
}

