package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Asterisk.asterisk;
import static fr.awildelephant.rdbms.ast.QualifiedColumnName.qualifiedColumnName;
import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.TableReferenceList.tableReferenceList;
import static fr.awildelephant.rdbms.ast.With.with;
import static fr.awildelephant.rdbms.ast.WithElement.withElement;
import static fr.awildelephant.rdbms.ast.WithList.withList;
import static fr.awildelephant.rdbms.ast.value.Equal.equal;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.columns;

public class WithParserTest {

    @Test
    void it_should_parse_a_with_statement_with_a_single_element() {
        assertParsing("WITH with_query AS (SELECT a, b FROM t1) SELECT * FROM t2, with_query WHERE t2.a = with_query.a",
                      with(withList(List.of(withElement("with_query", select(columns("a", "b"),
                                                                             tableName("t1"),
                                                                             null,
                                                                             null,
                                                                             null,
                                                                             null)))),
                           select(List.of(asterisk()),
                                  tableReferenceList(tableName("t2"), tableName("with_query"), List.of()),
                                  equal(qualifiedColumnName("t2", "a"), qualifiedColumnName("with_query", "a")),
                                  null,
                                  null,
                                  null)));
    }
}
