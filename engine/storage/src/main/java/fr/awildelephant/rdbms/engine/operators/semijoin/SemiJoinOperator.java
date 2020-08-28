package fr.awildelephant.rdbms.engine.operators.semijoin;

import fr.awildelephant.rdbms.engine.data.column.BooleanColumn;
import fr.awildelephant.rdbms.engine.data.column.Column;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.ColumnBasedTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.data.value.FalseValue.falseValue;
import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;

public final class SemiJoinOperator {

    private Schema outputSchema;
    private final SemiJoinMatcher matcher;

    public SemiJoinOperator(Schema outputSchema, SemiJoinMatcher matcher) {
        this.outputSchema = outputSchema;
        this.matcher = matcher;
    }

    public Table compute(Table left) {
        final List<Column> inputColumns = left.columns();

        final List<Column> outputColumns = new ArrayList<>(inputColumns.size() + 1);
        outputColumns.addAll(inputColumns);
        final BooleanColumn resultColumn = new BooleanColumn(left.numberOfTuples());
        outputColumns.add(resultColumn);

        for (Record record : left) {
            if (matcher.match(record)) {
                resultColumn.add(trueValue());
            } else {
                resultColumn.add(falseValue());
            }
        }

        return new ColumnBasedTable(outputSchema, outputColumns);
    }

}
