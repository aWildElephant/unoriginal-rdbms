package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.column.Column;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.ColumnBasedTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.values.RecordValues;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.createColumn;

public final class MapOperator implements Operator<Table, Table> {

    private final List<Formula> operations;
    private final Schema outputSchema;

    public MapOperator(List<Formula> operations, Schema outputSchema) {
        this.operations = operations;
        this.outputSchema = outputSchema;
    }

    @Override
    public Table compute(Table inputTable) {
        final List<Column> outputColumns = new ArrayList<>(outputSchema.numberOfAttributes());
        outputColumns.addAll(inputTable.columns());

        final List<ColumnReference> outputColumnReferences = outputSchema.columnNames();
        final List<ColumnReference> mapColumnReferences = outputColumnReferences.subList(inputTable.schema().numberOfAttributes(), outputColumnReferences.size());

        for (int i = 0; i < operations.size(); i++) {
            final Formula operation = operations.get(i);
            final ColumnReference columnReference = mapColumnReferences.get(i);
            final ColumnMetadata columnMetadata = outputSchema.column(columnReference).metadata();

            outputColumns.add(createColumnForOperation(operation, columnMetadata, inputTable));
        }

        return new ColumnBasedTable(outputSchema, outputColumns);
    }

    private Column createColumnForOperation(Formula operation, ColumnMetadata outputColumn, Table inputTable) {
        final Column column = createColumn(outputColumn, inputTable.numberOfTuples());

        final RecordValues values = new RecordValues();

        for (Record record : inputTable) {
            values.setRecord(record);

            column.add(operation.evaluate(values));
        }

        return column;
    }
}
