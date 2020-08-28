package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class TableConstructorLop extends AbstractLop {

    private final List<List<ValueExpression>> matrix;

    public TableConstructorLop(List<List<ValueExpression>> matrix) {
        super(createSchema(matrix));

        this.matrix = matrix;
    }

    private static Schema createSchema(List<List<ValueExpression>> matrix) {
        final ArrayList<ColumnMetadata> columns = new ArrayList<>();
        final List<ValueExpression> firstRow = matrix.get(0);

        final List<Domain> columnTypes = determineColumnTypes(matrix);

        for (int i = 0; i < firstRow.size(); i++) {
            final UnqualifiedColumnReference columnName = new UnqualifiedColumnReference("column" + (i + 1));

            // TODO: try to determine whether or not the formula is nullable
            columns.add(new ColumnMetadata(i, columnName, columnTypes.get(i), false, false));
        }

        return new Schema(columns);
    }

    private static List<Domain> determineColumnTypes(List<List<ValueExpression>> matrix) {
        final List<ValueExpression> firstRow = matrix.get(0);
        final List<Domain> columnTypes = new ArrayList<>(firstRow.size());

        for (ValueExpression cell : firstRow) {
            columnTypes.add(cell.domain());
        }

        for (int rowIndex = 1; rowIndex < matrix.size(); rowIndex++) {
            final List<ValueExpression> row = matrix.get(rowIndex);

            for (int columnIndex = 0; columnIndex < row.size(); columnIndex++) {
                final Domain columnType = columnTypes.get(columnIndex);

                final Domain cellType = row.get(columnIndex).domain();

                if (!cellType.canBeUsedAs(columnType)) {
                    if (columnType.canBeUsedAs(cellType)) {
                        columnTypes.set(columnIndex, cellType);
                    } else {
                        throw new IllegalStateException();
                    }
                }
            }
        }

        return columnTypes;
    }

    public List<List<ValueExpression>> matrix() {
        return matrix;
    }

    @Override
    public LogicalOperator transformInputs(Function<LogicalOperator, LogicalOperator> transformer) {
        return this;
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
