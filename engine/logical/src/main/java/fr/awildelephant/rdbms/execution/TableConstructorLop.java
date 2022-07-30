package fr.awildelephant.rdbms.execution;

import fr.awildelephant.rdbms.execution.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class TableConstructorLop extends AbstractLop {

    private final List<List<ValueExpression>> matrix;

    public TableConstructorLop(List<List<ValueExpression>> matrix) {
        super(createSchema(matrix));

        this.matrix = matrix;
    }

    private static Schema createSchema(List<List<ValueExpression>> matrix) {
        if (matrix.isEmpty()) {
            return Schema.EMPTY_SCHEMA;
        }

        final ArrayList<ColumnMetadata> columns = new ArrayList<>();

        final List<ValueExpression> firstRow = matrix.get(0);

        final List<Domain> columnTypes = determineColumnTypes(matrix);

        for (int i = 0; i < firstRow.size(); i++) {
            final UnqualifiedColumnReference columnName = new UnqualifiedColumnReference("column" + (i + 1));

            // TODO: try to determine whether or not the formula is nullable
            columns.add(new ColumnMetadata(columnName, columnTypes.get(i), false, false));
        }

        return Schema.of(columns);
    }

    private static List<Domain> determineColumnTypes(List<List<ValueExpression>> matrix) {
        final List<ValueExpression> firstRow = matrix.get(0);
        final List<Domain> columnTypes = new ArrayList<>(firstRow.size());

        for (ValueExpression cell : firstRow) {
            columnTypes.add(cell.domain());
        }

        final int numberOfRows = matrix.size();
        final int numberOfColumns = checkColumnCountForEachRow(matrix);

        if (numberOfColumns > 0) {
            for (int rowIndex = 1; rowIndex < numberOfRows; rowIndex++) {
                final List<ValueExpression> row = matrix.get(rowIndex);

                for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
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
        }

        return columnTypes;
    }

    private static int checkColumnCountForEachRow(List<List<ValueExpression>> matrix) {
        if (matrix.isEmpty()) {
            throw new UnsupportedOperationException("Empty table constructor");
        }

        final int numberOfColumns = matrix.get(0).size();

        for (int i = 1; i < matrix.size(); i++) {
            if (matrix.get(i).size() != numberOfColumns) {
                throw new UnsupportedOperationException("Column count does not match");
            }
        }

        return numberOfColumns;
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
        if (!(obj instanceof final TableConstructorLop other)) {
            return false;
        }

        return Objects.equals(matrix, other.matrix);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("matrix", matrix)
                .toString();
    }
}
