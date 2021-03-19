package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.column.Column;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.join.JoinMatcher;
import fr.awildelephant.rdbms.engine.operators.join.JoinRow;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;

public final class InnerJoinOperator {

    private final Function<Table, JoinMatcher> matcherCreator;
    private final Schema outputSchema;

    public InnerJoinOperator(Function<Table, JoinMatcher> matcherCreator, Schema outputSchema) {
        this.matcherCreator = matcherCreator;
        this.outputSchema = outputSchema;
    }

    public Table compute(Table leftInput, Table rightInput) {
        final JoinMatcher matcher = matcherCreator.apply(rightInput);

        final List<JoinRow> joinedRows = new ArrayList<>();

        for (Record leftRow : leftInput) {
            final List<Record> rightRows = matcher.match(leftRow);

            for (Record rightRow : rightRows) {
                joinedRows.add(new JoinRow(leftRow.materialize(), rightRow));
            }
        }

        final Table outputTable = simpleTable(outputSchema, joinedRows.size());

        final List<Column> outputColumns = outputTable.columns();
        final int numberOfColumnsLeftInput = leftInput.schema().numberOfAttributes();
        for (JoinRow joinedRow : joinedRows) {
            final Record leftRow = joinedRow.leftRow();
            for (int leftInputColumnIndex = 0; leftInputColumnIndex < leftRow.size(); leftInputColumnIndex++) {
                outputColumns.get(leftInputColumnIndex).add(leftRow.get(leftInputColumnIndex));
            }

            final Record rightRow = joinedRow.rightRow();
            for (int rightInputColumnIndex = 0; rightInputColumnIndex < rightRow.size(); rightInputColumnIndex++) {
                outputColumns.get(numberOfColumnsLeftInput + rightInputColumnIndex).add(rightRow.get(rightInputColumnIndex));
            }
        }

        return outputTable;
    }
}
