package fr.awildelephant.rdbms.test.commons;

import java.util.List;

// TODO: refactor to make columnar (?)
public record ExpectedResult(List<String> columnNames, List<Checker> columnTypes, List<List<String>> data) {

}
