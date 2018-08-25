package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.List;
import java.util.Map;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class MapOperator implements Operator {

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

        for (Record record : inputTable) {
            final Record extendedRecord = computeMaps(record, inputSchema);
            outputTable.add(extendedRecord);
        }

        return outputTable;
    }

    private Record computeMaps(Record record, Schema inputSchema) {
        final int numberOfInputAttributes = inputSchema.numberOfAttributes();
        final int numberOfOutputAttributes = outputSchema.numberOfAttributes();

        final DomainValue[] outputValues = new DomainValue[numberOfOutputAttributes];

        for (int i = 0; i < numberOfInputAttributes; i++) {
            outputValues[i] = record.get(i);
        }

        final Map<String, DomainValue> values = columnToValue(record, inputSchema);

        for (int i = 0; i < operations.size(); i++) {
            outputValues[numberOfInputAttributes + i] = operations.get(i).evaluate(values);
        }

        return new Record(outputValues);
    }

    private Map<String, DomainValue> columnToValue(Record record, Schema inputSchema) {
        return inputSchema.columnNames()
                          .stream()
                          .collect(toMap(identity(), name -> record.get(inputSchema.indexOf(name))));
    }
}
