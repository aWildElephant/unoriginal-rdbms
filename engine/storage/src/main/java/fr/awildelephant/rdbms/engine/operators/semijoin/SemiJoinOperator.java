package fr.awildelephant.rdbms.engine.operators.semijoin;

import fr.awildelephant.rdbms.engine.data.column.BooleanColumn;
import fr.awildelephant.rdbms.engine.data.column.Column;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.NewColumnBasedTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.data.value.FalseValue.falseValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.TRUE;

public final class SemiJoinOperator {

    private final Schema outputSchema;
    private final SemiJoinMatcher matcher;

    public SemiJoinOperator(Schema outputSchema, SemiJoinMatcher matcher) {
        this.outputSchema = outputSchema;
        this.matcher = matcher;
    }

    public Table compute(Table left) {
        final List<? extends Column> inputColumns = left.columns();

        final List<Column> outputColumns = new ArrayList<>(inputColumns.size() + 1);
        outputColumns.addAll(inputColumns);
        final BooleanColumn resultColumn = new BooleanColumn(left.numberOfTuples());
        outputColumns.add(resultColumn);

        for (Record record : left) {
            final ThreeValuedLogic result = matcher.match(record);
            if (result == TRUE) {
                resultColumn.add(trueValue());
            } else if (result == FALSE) {
                resultColumn.add(falseValue());
            } else {
                resultColumn.add(nullValue());
            }
        }

        return new NewColumnBasedTable(outputSchema, outputColumns);
    }

}
