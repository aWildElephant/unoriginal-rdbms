package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;
import static fr.awildelephant.rdbms.engine.operators.JoinUtils.joinRecords;

public class InnerHashJoinOperator implements JoinOperator {

    private final ColumnReference leftJoinColumn;
    private final ColumnReference rightJoinColumn;
    private final Schema outputSchema;

    public InnerHashJoinOperator(ColumnReference leftJoinColumn, ColumnReference rightJoinColumn, Schema outputSchema) {
        this.leftJoinColumn = leftJoinColumn;
        this.rightJoinColumn = rightJoinColumn;
        this.outputSchema = outputSchema;

    }

    @Override
    public Table compute(Table left, Table right) {
        final Map<DomainValue, List<Record>> hash = hash(right, rightJoinColumn);

        final int index = left.schema().column(leftJoinColumn).index();

        final Table outputTable = simpleTable(outputSchema);

        for (Record record : left) {
            final List<Record> matchingRecords = hash.get(record.get(index));
            if (matchingRecords != null) {
                for (Record matchingRecord : matchingRecords) {
                    outputTable.add(joinRecords(record, matchingRecord));
                }
            }
        }

        return outputTable;
    }

    private Map<DomainValue, List<Record>> hash(Table table, ColumnReference column) {
        final int index = table.schema().column(column).index();

        // TODO: utiliser un index de la table si possible
        final HashMap<DomainValue, List<Record>> hash = new HashMap<>();

        for (Record record : table) {
            hash.computeIfAbsent(record.get(index), ignored -> new ArrayList<>())
                .add(record);
        }

        return hash;
    }
}
