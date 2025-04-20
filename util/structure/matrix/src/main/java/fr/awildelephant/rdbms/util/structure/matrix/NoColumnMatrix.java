package fr.awildelephant.rdbms.util.structure.matrix;

public final class NoColumnMatrix<CELL_TYPE> implements Matrix<CELL_TYPE> {

    private final int numberOfRows;

    public NoColumnMatrix(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    @Override
    public CELL_TYPE get(int x, int y) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int numberOfColumns() {
        return 0;
    }

    @Override
    public int numberOfRows() {
        return numberOfRows;
    }
}
