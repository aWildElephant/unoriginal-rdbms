package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

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
        // TODO: can we use Schema#extend
        final List<ColumnReference> inputColumns = schema.columnNames();

        final List<ColumnMetadata> outputColumns = new ArrayList<>(inputColumns.size() + valueExpressions.size());

        for (ColumnReference columnReference : inputColumns) {
            outputColumns.add(schema.column(columnReference));
        }

        int index = inputColumns.size();

        for (int i = 0; i < valueExpressions.size(); i++) {
            outputColumns.add(new ColumnMetadata(index++, new UnqualifiedColumnReference(outputNames.get(i)),
                                         valueExpressions.get(i).domain(), false, false));
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
    public LogicalOperator transformInputs(Function<LogicalOperator, LogicalOperator> transformer) {
        return new MapLop(transformer.apply(input), expressions, expressionsOutputNames);
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, expressions, expressionsOutputNames);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MapLop)) {
            return false;
        }

        final MapLop other = (MapLop) obj;

        return Objects.equals(input, other.input)
                && Objects.equals(expressions, other.expressions)
                && Objects.equals(expressionsOutputNames, other.expressionsOutputNames);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("input", input)
                .append("expressions", expressions)
                .append("aliases", expressionsOutputNames)
                .toString();
    }
}
