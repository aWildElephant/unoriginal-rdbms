package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.Column;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class TableConstructorLop extends AbstractLop {

    private final List<List<ValueExpression>> matrix;

    public TableConstructorLop(List<List<ValueExpression>> matrix) {
        super(createSchema(matrix));

        this.matrix = matrix;
    }

    private static Schema createSchema(List<List<ValueExpression>> matrix) {
        final ArrayList<Column> columns = new ArrayList<>();
        final List<ValueExpression> firstRow = matrix.get(0);

        for (int i = 0; i < firstRow.size(); i++) {
            final ValueExpression formula = firstRow.get(i);

            // TODO: try to determine whether or not the formula is nullable
            columns.add(new Column(i, new UnqualifiedColumnReference("column" + (i + 1)), formula.domain(),
                                   true, false));
        }

        return new Schema(columns);
    }

    public List<List<ValueExpression>> matrix() {
        return matrix;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(matrix);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TableConstructorLop)) {
            return false;
        }

        final TableConstructorLop other = (TableConstructorLop) obj;

        return Objects.equals(matrix, other.matrix);
    }
}
