package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.PlanExecutor;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.subquery.LogicalOperatorOuterQueryReferenceReplacer;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.schema.Schema;

import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;

public class SubqueryExecutionOperator implements Operator<Table, Table> {

    private final LogicalOperator subquery;
    private final PlanExecutor executor;
    private final Schema outputSchema;

    public SubqueryExecutionOperator(LogicalOperator subquery, PlanExecutor executor, Schema outputSchema) {
        this.subquery = subquery;
        this.executor = executor;
        this.outputSchema = outputSchema;
    }

    @Override
    public Table compute(Table input) {
        final Table output = simpleTable(outputSchema, input.numberOfTuples());
        final Schema inputSchema = input.schema();
        final int numberOfInputColumns = inputSchema.numberOfAttributes();
        final int numberOfOutputColumns = outputSchema.numberOfAttributes();

        for (Record record : input) {
            final DomainValue[] values = new DomainValue[numberOfOutputColumns];

            for (int i = 0; i < numberOfInputColumns; i++) {
                values[i] = record.get(i);
            }

            final LogicalOperator transformedSubquery = replaceOuterQueryReferences(inputSchema, record);

            final Table subqueryResult = executor.apply(transformedSubquery);

            if (subqueryResult.numberOfTuples() > 0) {
                values[numberOfOutputColumns - 1] = subqueryResult.iterator().next().get(0);
            } else {
                values[numberOfOutputColumns - 1] = nullValue();
            }

            output.add(new Record(values));
        }

        return output;
    }

    private LogicalOperator replaceOuterQueryReferences(Schema inputSchema, Record record) {
        return new LogicalOperatorOuterQueryReferenceReplacer(inputSchema, record).apply(subquery);
    }
}
