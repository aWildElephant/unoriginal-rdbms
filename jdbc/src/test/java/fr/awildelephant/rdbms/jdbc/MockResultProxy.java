package fr.awildelephant.rdbms.jdbc;

import fr.awildelephant.rdbms.jdbc.abstraction.ResultProxy;
import fr.awildelephant.rdbms.jdbc.abstraction.Value;
import fr.awildelephant.rdbms.util.structure.matrix.Matrix;

/**
 * A custom ResultProxy used for testing.
 */
public final class MockResultProxy implements ResultProxy {

    private final Matrix<Value> matrix;

    public MockResultProxy(Matrix<Value> matrix) {
        this.matrix = matrix;
    }

    @Override
    public Value get(int row, int column) {
        return matrix.get(column, row);
    }

    @Override
    public int position(String columnName) {
        return 0;
    }

    @Override
    public String columnName(int column) {
        return "";
    }

    @Override
    public String columnTypeName(int column) {
        return "";
    }

    @Override
    public boolean isNullable(int column) {
        return false;
    }

    @Override
    public int numberOfRows() {
        return matrix.numberOfRows();
    }

    @Override
    public int numberOfColumns() {
        return matrix.numberOfColumns();
    }
}
