package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.Column;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;

public final class MapLop extends AbstractLop {

    private final LogicalOperator input;
    private final List<ValueExpression> expressions;
    private final List<String> expressionsOutputNames;

    public MapLop(LogicalOperator input, List<ValueExpression> expressions, List<String> expressionsOutputNames) {
        super(buildOutputSchema(expressions, expressionsOutputNames, input.schema()));

        this.expressions = expressions;
        this.expressionsOutputNames = expressionsOutputNames;
        this.input = input;

    }

    private static Schema buildOutputSchema(List<ValueExpression> valueExpressions, List<String> outputNames, Schema schema) {
        final List<String> inputColumns = schema.columnNames();

        final List<Column> outputColumns = new ArrayList<>(inputColumns.size() + valueExpressions.size());

        for (String columnName : inputColumns) {
            outputColumns.add(schema.column(columnName));
        }

        int index = inputColumns.size();

        for (int i = 0; i < valueExpressions.size(); i++) {
            outputColumns.add(new Column(index++, outputNames.get(i), valueExpressions.get(i).domain(), false));
        }

        return new Schema(outputColumns);
    }

    public LogicalOperator input() {
        return input;
    }

    public List<ValueExpression> expressions() {
        return expressions;
    }

    public List<String> expressionsOutputNames() {
        return expressionsOutputNames;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
