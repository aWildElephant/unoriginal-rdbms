package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.table.ListTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.data.tuple.Tuple;
import fr.awildelephant.rdbms.engine.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.List;

public class ProjectionOperator implements Operator {

    private final Schema outputSchema;
    private int[] outputToInputIndex;

    public ProjectionOperator(Schema outputSchema) {
        this.outputSchema = outputSchema;
    }

    @Override
    public ListTable compute(Table inputTable) {
        this.outputToInputIndex = buildRelativeMapping(inputTable.schema());
        final ListTable outputTable = new ListTable(outputSchema, inputTable.numberOfTuples());

        for (Tuple tuple : inputTable) {
            outputTable.add(projectTuple(tuple));
        }

        return outputTable;
    }

    private Tuple projectTuple(Tuple tuple) {
        final DomainValue[] data = new DomainValue[outputToInputIndex.length];

        for (int i = 0; i < data.length; i++) {
            data[i] = tuple.get(outputToInputIndex[i]);
        }

        return new Tuple(outputSchema, data);
    }

    private int[] buildRelativeMapping(Schema inputSchema) {
        final List<String> outputColumns = outputSchema.columnNames();
        final int[] mapping = new int[outputColumns.size()];

        for (String column : outputColumns) {
            mapping[outputSchema.indexOf(column)] = inputSchema.indexOf(column);
        }

        return mapping;
    }
}
