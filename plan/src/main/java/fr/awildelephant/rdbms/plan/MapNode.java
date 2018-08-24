package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.schema.Column;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;

public final class MapNode extends AbstractPlan {

    private final List<Formula> operations;
    private final Plan input;

    public MapNode(List<Formula> operations, Plan input) {
        super(buildOutputSchema(operations, input.schema()));

        this.operations = operations;
        this.input = input;
    }

    private static Schema buildOutputSchema(List<Formula> operations, Schema schema) {
        final List<String> inputColumns = schema.columnNames();

        final List<Column> outputColumns = new ArrayList<>(inputColumns.size() + operations.size());

        for (String columnName : inputColumns) {
            outputColumns.add(schema.column(columnName));
        }

        int index = inputColumns.size();

        for (Formula operation : operations) {
            outputColumns.add(new Column(index, operation.outputName(), operation.outputType(), false));
        }

        return new Schema(outputColumns);
    }

    public List<Formula> operations() {
        return operations;
    }

    public Plan input() {
        return input;
    }

    @Override
    public <T> T accept(PlanVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
