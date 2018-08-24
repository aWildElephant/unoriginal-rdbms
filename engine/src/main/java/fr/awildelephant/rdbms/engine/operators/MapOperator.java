package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.List;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;

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

        for (Record record : inputTable) {
            final Record extendedRecord = computeMaps(record);

            outputTable.add(extendedRecord);
        }

        return outputTable;
    }

    private Record computeMaps(Record record) {
        final int numberOfOutputAttributes = outputSchema.numberOfAttributes();
        final int numberOfInputAttributes = numberOfOutputAttributes - operations.size();

        final DomainValue[] outputValues = new DomainValue[numberOfOutputAttributes];

        for (int i = 0; i < numberOfInputAttributes; i++) {
            outputValues[i] = record.get(i);
        }

        for (int i = 0; i < operations.size(); i++) {
            outputValues[numberOfInputAttributes + i] = operations.get(i).evaluate();
        }

        return new Record(outputValues);
    }
}
