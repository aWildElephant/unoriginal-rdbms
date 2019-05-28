package fr.awildelephant.rdbms.jdbc.abstraction;

public interface ResultProxy {

    Value get(int row, int column);

    /**
     * @return column position starting from 0. Returns -1 if there is no column with the given name
     */
    int position(String columnName);

    String columnName(int column);

    String columnTypeName(int column);

    boolean isNullable(int column);

    int numberOfRows();

    int numberOfColumns();
}
