package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.table.ColumnBasedTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.schema.Schema;

// TODO: the AliasOperator should probably disappear and the alias be set directly by BaseTableOperator or TableConstructorOperator
public final class AliasOperator implements Operator<Table, Table> {

    private final Schema schema;

    public AliasOperator(Schema schema) {
        this.schema = schema;
    }

    @Override
    public Table compute(Table inputTable) {
        return new ColumnBasedTable(schema, inputTable.columns());
    }
}
