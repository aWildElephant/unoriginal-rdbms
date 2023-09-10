package fr.awildelephant.rdbms.execution.operator;

import fr.awildelephant.rdbms.execution.operator.join.JoinMatcher;
import fr.awildelephant.rdbms.execution.operator.join.JoinOutputCreator;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.storage.data.chunk.Chunk;
import fr.awildelephant.rdbms.storage.data.record.Record;
import fr.awildelephant.rdbms.storage.data.table.Table;
import fr.awildelephant.rdbms.storage.data.table.WriteableTable;

import static fr.awildelephant.rdbms.storage.data.table.TableFactory.simpleTable;

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
        final WriteableTable outputTable = simpleTable(outputSchema);
        for (Record leftRecord : leftInput) {
            final Chunk<Record> matchingRightRecords = matcher.match(leftRecord);

            outputTable.addAll(outputCreator.join(leftRecord, matchingRightRecords));
        }
        return outputTable;
    }
}
