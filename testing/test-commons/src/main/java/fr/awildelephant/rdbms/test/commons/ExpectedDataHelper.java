package fr.awildelephant.rdbms.test.commons;

import fr.awildelephant.rdbms.util.structure.matrix.Matrix;
import fr.awildelephant.rdbms.util.structure.matrix.MatrixFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class ExpectedDataHelper {

    private ExpectedDataHelper() {

    }

    public static ExpectedResult fromRowBasedData(@NotNull final List<List<String>> data) {
        final List<String> columnNames = data.getFirst();
        final List<String> columnTypes = data.get(1);

        final int numberOfColumns = columnNames.size();
        final List<ExpectedColumn> columns = new ArrayList<>(numberOfColumns);
        for (int i = 0; i < numberOfColumns; i++) {
            columns.add(new ExpectedColumn(columnNames.get(i), Checker.checkerFor(columnTypes.get(i))));
        }

        final Matrix<String> content = MatrixFactory.fromRowBasedData(data.subList(2, data.size()));

        return new ExpectedResult(columns, content);
    }
}
