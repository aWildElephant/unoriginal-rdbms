package fr.awildelephant.rdbms.util.structure.matrix;

import java.util.List;

/**
 * A column-based matrix, with at least one column.
 */
public final class ColumnBasedMatrix<CELL_TYPE> extends ListBackedMatrix<CELL_TYPE> {

    public ColumnBasedMatrix(List<List<CELL_TYPE>> backingLists) {
        super(backingLists);
    }

    @Override
    public CELL_TYPE get(int x, int y) {
        return backingLists.get(x).get(y);
    }

    @Override
    public int numberOfColumns() {
        return backingLists.size();
    }

    @Override
    public int numberOfRows() {
        return backingLists.getFirst().size();
    }
}
