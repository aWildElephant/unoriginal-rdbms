package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Asterisk.asterisk;
import static fr.awildelephant.rdbms.ast.QualifiedColumnName.qualifiedColumnName;
import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.value.Any.any;
import static fr.awildelephant.rdbms.ast.value.Less.less;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class AnyAllParserTest {

    @Test
    void it_should_parse_an_any_aggregate() {
        assertParsing("SELECT ANY(a) FROM test",

                select(List.of(any(unqualifiedColumnName("a"))), tableName("test"), null, null, null, null));
    }

    @Test
    void it_should_parse_any_in_the_where_clause() {
        assertParsing("SELECT * FROM main where main.c1 < ANY(SELECT other.c1 FROM other)",

                // TODO: a builder for the select node
                select(List.of(asterisk()),
                        tableName("main"),
                        less(qualifiedColumnName("main", "c1"), any(select(List.of(qualifiedColumnName("other", "c1")),
                                tableName("other"),
                                null,
                                null,
                                null,
                                null))),
                        null,
                        null,
                        null));
    }
}
