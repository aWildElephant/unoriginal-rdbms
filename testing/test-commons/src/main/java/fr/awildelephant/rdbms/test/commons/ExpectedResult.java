package fr.awildelephant.rdbms.test.commons;

import fr.awildelephant.rdbms.util.structure.matrix.Matrix;

import java.util.List;

public record ExpectedResult(List<ExpectedColumn> columns, Matrix<String> content) {

}
