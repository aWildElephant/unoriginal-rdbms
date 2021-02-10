package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.QualifiedColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class ScalarSubqueryLop extends AbstractLop {

    private final LogicalOperator input;
    private final String id;

    public ScalarSubqueryLop(LogicalOperator input, String id) {
        super(checkAndCreateSchema(input, id));

        this.input = input;
        this.id = id;
    }

    private static Schema checkAndCreateSchema(LogicalOperator input, String id) {
        final List<ColumnReference> columns = input.schema().columnNames();

        if (columns.size() > 1) {
            throw new IllegalArgumentException("Scalar subquery cannot have more than one column");
        }

        final ColumnMetadata column = input.schema().column(columns.get(0));

        return new Schema(List.of(new ColumnMetadata(0, new QualifiedColumnReference(id, "0"), column.domain(),
                                                     column.notNull(), true)));
    }

    public LogicalOperator input() {
        return input;
    }

    public String id() {
        return id;
    }

    @Override
    public LogicalOperator transformInputs(Function<LogicalOperator, LogicalOperator> transformer) {
        return new ScalarSubqueryLop(transformer.apply(input), id);
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, id);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ScalarSubqueryLop)) {
            return false;
        }

        final ScalarSubqueryLop other = (ScalarSubqueryLop) obj;

        return Objects.equals(input, other.input)
                && Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("subqueryId", id)
                .append("input", input)
                .toString();
    }
}
