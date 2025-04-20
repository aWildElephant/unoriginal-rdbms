package fr.awildelephant.rdbms.util.structure.matrix;

import java.util.ArrayList;
import java.util.List;

public final class MatrixFactory {

    private MatrixFactory() {

    }

    public static <CELL_TYPE> Matrix<CELL_TYPE> fromRowBasedData(final int numberOfRows, final int numberOfColumns, CELL_TYPE... values) {
        if (values.length != numberOfRows * numberOfColumns) {
            throw new IllegalArgumentException();
        }

        if (numberOfColumns == 0) {
            return new NoColumnMatrix<>(numberOfRows);
        }

        final List<List<CELL_TYPE>> columns = new ArrayList<>(numberOfColumns);
        for (int i = 0; i < numberOfColumns; i++) {
            final List<CELL_TYPE> column = new ArrayList<>(numberOfRows);
            for (int j = 0; j < numberOfRows; j++) {
                column.add(values[numberOfColumns * j + i]);
            }
            columns.add(column);
        }

        return new ColumnBasedMatrix<>(columns);
    }

    public static <CELL_TYPE> Matrix<CELL_TYPE> fromRowBasedData(final List<List<CELL_TYPE>> rows) {
        if (rows.isEmpty()) {
            return new NoColumnMatrix<>(0);
        }
        final int numberOfRows = rows.size();
        final List<CELL_TYPE> first = rows.getFirst();
        if (first.isEmpty()) {
            return new NoColumnMatrix<>(numberOfRows);
        }
        final int numberOfColumns = first.size();
        final List<List<CELL_TYPE>> columns = new ArrayList<>(numberOfColumns);
        for (int i = 0; i < numberOfColumns; i++) {
            columns.add(new ArrayList<>(numberOfRows));
        }
        for (List<CELL_TYPE> row : rows) {
            for (int i = 0; i < numberOfColumns; i++) {
                columns.get(i).add(row.get(i));
            }
        }
        return new ColumnBasedMatrix<>(columns);
    }
}
