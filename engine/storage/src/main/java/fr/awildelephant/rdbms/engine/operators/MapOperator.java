package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.values.RecordValues;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.List;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;

public class MapOperator implements Operator<Table, Table> {

    private final List<Formula> operations;
    private final Schema outputSchema;

    public MapOperator(List<Formula> operations, Schema outputSchema) {
        this.operations = operations;
        this.outputSchema = outputSchema;
    }

    @Override
    public Table compute(Table inputTable) {
        final Table outputTable = simpleTable(outputSchema);
        final Schema inputSchema = inputTable.schema();
        final RecordValues values = new RecordValues();

        for (Record record : inputTable) {
            final Record extendedRecord = computeMaps(record, inputSchema, values);
            outputTable.add(extendedRecord);
        }

        return outputTable;
    }

    private Record computeMaps(Record record, Schema inputSchema, RecordValues values) {
        final int numberOfInputAttributes = inputSchema.numberOfAttributes();
        final int numberOfOutputAttributes = outputSchema.numberOfAttributes();

        final DomainValue[] outputValues = new DomainValue[numberOfOutputAttributes];

        for (int i = 0; i < numberOfInputAttributes; i++) {
            outputValues[i] = record.get(i);
        }

        values.setRecord(record);

        for (int i = 0; i < operations.size(); i++) {
            outputValues[numberOfInputAttributes + i] = operations.get(i).evaluate(values);
        }

        return new Record(outputValues);
    }
}
