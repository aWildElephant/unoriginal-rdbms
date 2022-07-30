package fr.awildelephant.rdbms.execution.operator;

import fr.awildelephant.rdbms.engine.data.column.Column;
import fr.awildelephant.rdbms.engine.data.table.NewColumnBasedTable;
import fr.awildelephant.rdbms.engine.data.table.NoColumnTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.execution.executor.TemporaryStorage;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;

public class ProjectionOperator implements Operator {

    private final String inputKey;
    private final Schema outputSchema;

    public ProjectionOperator(String inputKey, Schema outputSchema) {
        this.inputKey = inputKey;
        this.outputSchema = outputSchema;
    }

    @Override
    public Table compute(TemporaryStorage storage) {
        final Table inputTable = storage.get(inputKey);

        if (outputSchema.numberOfAttributes() == 0) {
            return new NoColumnTable(inputTable.numberOfTuples());
        }

        final List<? extends Column> inputColumns = inputTable.columns();

        final List<Column> outputColumns = new ArrayList<>(outputSchema.numberOfAttributes());

        final Schema inputSchema = inputTable.schema();
        for (ColumnReference columnReference : outputSchema.columnNames()) {
            outputColumns.add(inputColumns.get(inputSchema.column(columnReference).index()));
        }

        return new NewColumnBasedTable(outputSchema, outputColumns);
    }
}
