package fr.awildelephant.rdbms.util.structure.matrix;

import java.util.List;

public abstract class ListBackedMatrix<CELL_TYPE> implements Matrix<CELL_TYPE> {

    protected List<List<CELL_TYPE>> backingLists;

    protected ListBackedMatrix(final List<List<CELL_TYPE>> backingLists) {
        this.backingLists = backingLists;
    }
}
