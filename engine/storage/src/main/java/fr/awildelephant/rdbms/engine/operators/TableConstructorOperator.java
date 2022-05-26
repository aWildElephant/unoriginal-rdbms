package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.column.WriteableColumn;
import fr.awildelephant.rdbms.engine.data.table.ColumnBasedWriteableTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.data.table.TableFactory;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.List;

import static fr.awildelephant.rdbms.evaluator.input.NoValues.noValues;

public class TableConstructorOperator implements Operator<Void, Table> {

    private final List<List<Formula>> rowBasedMatrix;
    private final Schema schema;

    public TableConstructorOperator(List<List<Formula>> rowBasedMatrix, Schema schema) {
        this.rowBasedMatrix = rowBasedMatrix;
        this.schema = schema;
    }

    @Override
    public Table compute(Void unused) {
        final int numberOfRows = rowBasedMatrix.size();
        final List<WriteableColumn> columns = TableFactory.createColumns(schema, numberOfRows);

        for (List<Formula> row : rowBasedMatrix) {
            for (int columnIndex = 0; columnIndex < row.size(); columnIndex++) {
                final Formula cellFormula = row.get(columnIndex);

                columns.get(columnIndex).add(cellFormula.evaluate(noValues()));
            }
        }

        return new ColumnBasedWriteableTable(schema, columns);
    }
}
