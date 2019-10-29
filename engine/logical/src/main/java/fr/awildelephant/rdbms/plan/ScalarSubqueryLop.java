package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.Column;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;

import java.util.List;

public final class ScalarSubqueryLop extends AbstractLop {


    private final LogicalOperator input;

    public ScalarSubqueryLop(LogicalOperator input, String id) {
        super(checkAndCreateSchema(input, id));

        this.input = input;
    }

    private static Schema checkAndCreateSchema(LogicalOperator input, String id) {
        final List<ColumnReference> columns = input.schema().columnNames();

        if (columns.size() > 1) {
            throw new IllegalArgumentException("Scalar subquery cannot have more than one column");
        }

        final Column column = input.schema().column(columns.get(0));

        return new Schema(List.of(new Column(0, new UnqualifiedColumnReference(id), column.domain(),
                                             column.notNull(), true)));
    }

    public LogicalOperator input() {
        return input;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
