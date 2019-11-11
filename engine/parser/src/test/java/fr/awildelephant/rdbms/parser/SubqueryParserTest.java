package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Asterisk.asterisk;
import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.value.Equal.equal;
import static fr.awildelephant.rdbms.ast.value.Min.min;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class SubqueryParserTest {

    @Test
    void it_should_parse_a_subquery_in_the_where_clause_of_a_select_query() {
        assertParsing("SELECT * FROM test WHERE a = (SELECT MIN(b) FROM other)",

                      select(List.of(asterisk()),
                             tableName("test"),
                             equal(unqualifiedColumnName("a"),
                                   select(List.of(min(unqualifiedColumnName("b"))),
                                          tableName("other"),
                                          null,
                                          null,
                                          null,
                                          null)),
                             null,
                             null,
                             null));
    }
}
