package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.schema.Schema;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;

public class AliasOperator implements Operator {

    private final Schema schema;

    public AliasOperator(Schema schema) {
        this.schema = schema;
    }

    @Override
    public Table compute(Table inputTable) {
        final Table outputTable = simpleTable(schema, inputTable.numberOfTuples());

        // TODO: see how we could avoid to copy the table
        for (Record record : inputTable) {
            outputTable.add(record);
        }

        return outputTable;
    }
}
