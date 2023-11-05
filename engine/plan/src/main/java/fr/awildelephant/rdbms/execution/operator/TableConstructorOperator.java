package fr.awildelephant.rdbms.execution.operator;

import fr.awildelephant.rdbms.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.execution.executor.TemporaryStorage;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.storage.data.column.AppendableColumn;
import fr.awildelephant.rdbms.storage.data.table.ColumnBasedWriteableTable;
import fr.awildelephant.rdbms.storage.data.table.NoColumnTable;
import fr.awildelephant.rdbms.storage.data.table.Table;
import fr.awildelephant.rdbms.storage.data.table.TableFactory;

import java.util.List;

import static fr.awildelephant.rdbms.evaluator.input.NoValues.noValues;
import static fr.awildelephant.rdbms.formula.creation.ValueExpressionToFormulaTransformer.createFormula;
import static fr.awildelephant.rdbms.schema.Schema.EMPTY_SCHEMA;

public class TableConstructorOperator implements Operator {

    private final List<List<ValueExpression>> matrix;
    private final Schema outputSchema;

    public TableConstructorOperator(List<List<ValueExpression>> matrix, Schema outputSchema) {
        this.matrix = matrix;
        this.outputSchema = outputSchema;
    }

    @Override
    public Table compute(TemporaryStorage storage) {
        final List<List<Formula>> formulas = matrix.stream()
                .map(row -> row.stream()
                        .map(expression -> createFormula(expression,
                                EMPTY_SCHEMA))
                        .toList())
                .toList();

        final int numberOfRows = formulas.size();
        final int numberOfColumns = outputSchema.numberOfAttributes();

        if (numberOfColumns > 0) {
            final List<AppendableColumn> columns = TableFactory.createColumns(outputSchema, numberOfRows);

            for (List<Formula> row : formulas) {
                for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
                    final Formula cellFormula = row.get(columnIndex);

                    columns.get(columnIndex).add(cellFormula.evaluate(noValues()));
                }
            }

            return new ColumnBasedWriteableTable(outputSchema, columns);
        } else {
            return new NoColumnTable(numberOfRows);
        }
    }
}
