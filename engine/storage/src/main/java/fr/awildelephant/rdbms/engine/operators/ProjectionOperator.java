package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.column.Column;
import fr.awildelephant.rdbms.engine.data.table.NewColumnBasedTable;
import fr.awildelephant.rdbms.engine.data.table.NoColumnTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;

public class ProjectionOperator implements Operator<Table, Table> {

    private final Schema outputSchema;

    public ProjectionOperator(Schema outputSchema) {
        this.outputSchema = outputSchema;
    }

    @Override
    public Table compute(Table inputTable) {
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
