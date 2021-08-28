package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;
import static fr.awildelephant.rdbms.plan.OutputSchemaFactory.mapOutputSchema;

public final class MapLop extends AbstractLop {

    private final LogicalOperator input;
    private final List<ValueExpression> expressions;
    private final List<ColumnReference> expressionsOutputNames;

    public MapLop(LogicalOperator input,
                  List<ValueExpression> expressions,
                  List<ColumnReference> expressionsOutputNames) {
        super(mapOutputSchema(input.schema(), expressions, expressionsOutputNames));

        this.expressions = expressions;
        this.expressionsOutputNames = expressionsOutputNames;
        this.input = input;
    }

    public LogicalOperator input() {
        return input;
    }

    public List<ValueExpression> expressions() {
        return expressions;
    }

    public List<ColumnReference> expressionsOutputNames() {
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
        if (!(obj instanceof final MapLop other)) {
            return false;
        }

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
