package fr.awildelephant.rdbms.execution.operator;

import fr.awildelephant.rdbms.engine.data.column.AppendOnlyColumn;
import fr.awildelephant.rdbms.engine.data.column.Column;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.ColumnBasedTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.data.table.TableFactory;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.execution.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.execution.executor.TemporaryStorage;
import fr.awildelephant.rdbms.execution.operator.values.RecordValues;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.formula.creation.ValueExpressionToFormulaTransformer.createFormula;
import static java.util.stream.Collectors.toList;

public class MapOperator implements Operator {

    private final String inputKey;
    private final List<ValueExpression> expressions;
    private final Schema outputSchema;

    public MapOperator(String inputKey, List<ValueExpression> expressions, Schema outputSchema) {
        this.inputKey = inputKey;
        this.expressions = expressions;
        this.outputSchema = outputSchema;
    }


    @Override
    public Table compute(TemporaryStorage storage) {
        final Table inputTable = storage.get(inputKey);

        final List<Formula> operations = expressions
                .stream()
                .map(expression -> createFormula(expression, inputTable.schema()))
                .collect(toList());

        final List<AppendOnlyColumn> mapColumns = computeMapOperations(inputTable, operations);

        final List<? extends Column> inputColumns = inputTable.columns();

        final List<Column> outputColumns = new ArrayList<>(inputColumns.size() + mapColumns.size());
        outputColumns.addAll(inputColumns);
        outputColumns.addAll(mapColumns);

        return new ColumnBasedTable(outputSchema, outputColumns);
    }

    private List<AppendOnlyColumn> computeMapOperations(Table inputTable, List<Formula> operations) {
        final List<AppendOnlyColumn> mapColumns = buildMapOutputColumns(inputTable.numberOfTuples(), operations);

        final RecordValues values = new RecordValues();
        for (Record record : inputTable) {
            values.setRecord(record);

            for (int operationIndex = 0; operationIndex < operations.size(); operationIndex++) {
                mapColumns.get(operationIndex).add(operations.get(operationIndex).evaluate(values));
            }
        }
        return mapColumns;
    }

    private List<AppendOnlyColumn> buildMapOutputColumns(int numberOfInputTuples, List<Formula> operations) {
        final int numberOfOutputColumns = outputSchema.numberOfAttributes();
        final int numberOfOperations = operations.size();
        final List<ColumnReference> mapColumnReferences = outputSchema.columnNames()
                .subList(numberOfOutputColumns - numberOfOperations, numberOfOutputColumns);

        final List<AppendOnlyColumn> mapColumns = new ArrayList<>(numberOfOperations);
        for (ColumnReference mapColumnReference : mapColumnReferences) {
            final ColumnMetadata column = outputSchema.column(mapColumnReference).metadata();

            mapColumns.add(TableFactory.createColumn(column, numberOfInputTuples));
        }
        return mapColumns;
    }
}
