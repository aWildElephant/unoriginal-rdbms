package fr.awildelephant.rdbms.util.structure.matrix;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MatrixFactoryTest {

    @Test
    void fromRowBasedData() {
        final Matrix<String> matrix = MatrixFactory.fromRowBasedData(2, 3,
                "aa", "ab", "ac",
                "ba", "bb", "bc");

        assertThat(matrix.numberOfRows()).isEqualTo(2);
        assertThat(matrix.numberOfColumns()).isEqualTo(3);
        assertThat(matrix.get(1, 1)).isEqualTo("bb");
    }
}