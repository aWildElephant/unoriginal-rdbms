package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.List;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;

public class ProjectionOperator implements Operator {

    private final Schema outputSchema;
    private int[] outputToInputIndex;

    public ProjectionOperator(Schema outputSchema) {
        this.outputSchema = outputSchema;
    }

    @Override
    public Table compute(Table inputTable) {
        this.outputToInputIndex = buildRelativeMapping(inputTable.schema());
        final Table outputTable = simpleTable(outputSchema, inputTable.numberOfTuples());

        for (Record record : inputTable) {
            outputTable.add(projectTuple(record));
        }

        return outputTable;
    }

    private Record projectTuple(Record record) {
        final DomainValue[] data = new DomainValue[outputToInputIndex.length];

        for (int i = 0; i < data.length; i++) {
            data[i] = record.get(outputToInputIndex[i]);
        }

        return new Record(data);
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
