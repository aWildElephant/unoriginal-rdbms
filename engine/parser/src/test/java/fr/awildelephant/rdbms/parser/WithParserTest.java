package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Asterisk.asterisk;
import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.TableReferenceList.tableReferenceList;
import static fr.awildelephant.rdbms.ast.With.with;
import static fr.awildelephant.rdbms.ast.WithElement.withElement;
import static fr.awildelephant.rdbms.ast.WithList.withList;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.columns;

public class WithParserTest {

    @Test
    void it_should_parse_a_with_statement_with_a_single_element() {
        assertParsing("WITH with_query AS (SELECT a, b FROM t1) SELECT * FROM with_query",
                      with(withList(List.of(withElement("with_query", select(columns("a", "b"),
                                                                             tableName("t1"),
                                                                             null,
                                                                             null,
                                                                             null,
                                                                             null)))),
                           select(List.of(asterisk()),
                                  tableName("with_query"),
                                  null,
                                  null,
                                  null,
                                  null)));
    }

    @Test
    void it_should_parse_a_with_statement_with_several_elements() {
        assertParsing("WITH first AS (select * from t1), second AS (select * from t2), third AS (select * from t3) select * from t1, t2, t3",
                      with(withList(List.of(withElement("first", select(List.of(asterisk()),
                                                                        tableName("t1"),
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null)),
                                            withElement("second", select(List.of(asterisk()),
                                                                        tableName("t2"),
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null)),
                                            withElement("third", select(List.of(asterisk()),
                                                                        tableName("t3"),
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null)))),
                           select(List.of(asterisk()),
                                  tableReferenceList(tableName("t1"), tableName("t2"), List.of(tableName("t3"))),
                                  null, null, null, null)));
    }
}
