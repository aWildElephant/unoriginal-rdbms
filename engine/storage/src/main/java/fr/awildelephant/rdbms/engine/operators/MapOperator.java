package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.column.Column;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.ColumnBasedTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.data.table.TableFactory;
import fr.awildelephant.rdbms.engine.operators.values.RecordValues;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;

public final class MapOperator implements Operator<Table, Table> {

    private final List<Formula> operations;
    private final Schema outputSchema;

    public MapOperator(List<Formula> operations, Schema outputSchema) {
        this.operations = operations;
        this.outputSchema = outputSchema;
    }

    @Override
    public Table compute(Table inputTable) {
        final List<Column> mapColumns = computeMapOperations(inputTable);

        final List<Column> inputColumns = inputTable.columns();

        final List<Column> outputColumns = new ArrayList<>(inputColumns.size() + mapColumns.size());
        outputColumns.addAll(inputColumns);
        outputColumns.addAll(mapColumns);

        return new ColumnBasedTable(outputSchema, outputColumns);
    }

    private List<Column> computeMapOperations(Table inputTable) {
        final List<Column> mapColumns = buildMapOutputColumns(inputTable.numberOfTuples());

        final RecordValues values = new RecordValues();
        for (Record record : inputTable) {
            values.setRecord(record);

            for (int operationIndex = 0; operationIndex < operations.size(); operationIndex++) {
                mapColumns.get(operationIndex).add(operations.get(operationIndex).evaluate(values));
            }
        }
        return mapColumns;
    }

    private List<Column> buildMapOutputColumns(int numberOfInputTuples) {
        final int numberOfOutputColumns = outputSchema.numberOfAttributes();
        final int numberOfOperations = operations.size();
        final List<ColumnReference> mapColumnReferences = outputSchema.columnNames()
                .subList(numberOfOutputColumns - numberOfOperations, numberOfOutputColumns);

        final List<Column> mapColumns = new ArrayList<>(numberOfOperations);
        for (ColumnReference mapColumnReference : mapColumnReferences) {
            final ColumnMetadata column = outputSchema.column(mapColumnReference).metadata();

            mapColumns.add(TableFactory.createColumn(column, numberOfInputTuples));
        }
        return mapColumns;
    }
}
