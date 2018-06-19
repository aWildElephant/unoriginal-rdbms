package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.Table;
import fr.awildelephant.rdbms.engine.data.Tuple;
import fr.awildelephant.rdbms.engine.data.domain.DomainValue;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.Set;

public class ProjectionOperator implements Operator {

    private final Schema outputSchema;
    private final int[] outputToInputIndex;

    public ProjectionOperator(Schema inputSchema, Schema outputSchema) {
        this.outputSchema = outputSchema;
        this.outputToInputIndex = buildRelativeMapping(inputSchema);
    }

    @Override
    public Table compute(Table inputTable) {
        final Table outputTable = new Table(outputSchema, inputTable.size());

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
        final Set<String> outputColumns = outputSchema.attributes();
        final int[] mapping = new int[outputColumns.size()];

        for (String column : outputColumns) {
            mapping[outputSchema.indexOf(column)] = inputSchema.indexOf(column);
        }

        return mapping;
    }
}
