package fr.awildelephant.rdbms.execution.operator;

import fr.awildelephant.rdbms.execution.executor.TemporaryStorage;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.storage.data.column.Column;
import fr.awildelephant.rdbms.storage.data.table.ColumnBasedTable;
import fr.awildelephant.rdbms.storage.data.table.Table;

import java.util.ArrayList;
import java.util.List;

public class BaseTableOperator implements Operator {

    private final String tableName;

    public BaseTableOperator(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public Table compute(TemporaryStorage storage) {
        final Table inputTable = storage.get(tableName);

        final Schema inputSchema = inputTable.schema();

        final Schema outputSchema = inputSchema.removeSystemColumns();

        // FIXME: code duplicated with ProjectOperator
        final List<? extends Column> inputColumns = inputTable.columns();

        final List<Column> outputColumns = new ArrayList<>(outputSchema.numberOfAttributes());

        for (ColumnReference columnReference : outputSchema.columnNames()) {
            outputColumns.add(inputColumns.get(inputSchema.column(columnReference).index()));
        }

        return new ColumnBasedTable(outputSchema, outputColumns);
    }
}
