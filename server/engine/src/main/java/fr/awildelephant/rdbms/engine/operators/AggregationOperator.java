package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.schema.Schema;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;
import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;

/**
 * TODO: Very simple because we only support COUNT(*) with no breakdown for now
 */
public class AggregationOperator implements Operator {

    private final Schema outputSchema;

    public AggregationOperator(Schema outputSchema) {
        this.outputSchema = outputSchema;
    }

    @Override
    public Table compute(Table inputTable) {
        final Table outputTable = simpleTable(outputSchema);

        final DomainValue[] outputTuple = new DomainValue[]{integerValue(inputTable.numberOfTuples())};

        outputTable.add(new Record(outputTuple));

        return outputTable;
    }
}
