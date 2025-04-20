package fr.awildelephant.rdbms.util.structure.matrix;

public interface Matrix<CELL_TYPE> {

    CELL_TYPE get(int x, int y);

    int numberOfColumns();

    int numberOfRows();
}
