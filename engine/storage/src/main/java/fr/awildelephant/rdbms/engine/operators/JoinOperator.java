package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.join.JoinMatcher;
import fr.awildelephant.rdbms.engine.operators.join.JoinOutputCreator;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.List;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;

public final class JoinOperator {

    private final JoinMatcher matcher;
    private final JoinOutputCreator outputCreator;
    private final Schema outputSchema;

    public JoinOperator(JoinMatcher matcher, JoinOutputCreator outputCreator, Schema outputSchema) {
        this.matcher = matcher;
        this.outputCreator = outputCreator;
        this.outputSchema = outputSchema;
    }

    public Table compute(Table leftInput) {
        final Table outputTable = simpleTable(outputSchema);
        for (Record leftRecord : leftInput) {
            final List<Record> matchingRightRecords = matcher.match(leftRecord);

            outputTable.addAll(outputCreator.join(leftRecord, matchingRightRecords));
        }
        return outputTable;
    }
}
