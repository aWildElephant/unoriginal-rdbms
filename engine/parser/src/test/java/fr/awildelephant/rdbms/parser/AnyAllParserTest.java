package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.ast.QualifiedColumnName.qualifiedColumnName;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.builder.SelectBuilder.select;
import static fr.awildelephant.rdbms.ast.value.Any.any;
import static fr.awildelephant.rdbms.ast.value.Less.less;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class AnyAllParserTest {

    @Test
    void it_should_parse_an_any_aggregate() {
        assertParsing("SELECT ANY(a) FROM test",

                select().outputColumns(any(unqualifiedColumnName("a")))
                        .fromClause(tableName("test")));
    }

    @Test
    void it_should_parse_any_in_the_where_clause() {
        assertParsing("SELECT * FROM main where main.c1 < ANY(SELECT other.c1 FROM other)",

                select().fromClause(tableName("main"))
                        .whereClause(less(qualifiedColumnName("main", "c1"),
                                any(select().outputColumns(qualifiedColumnName("other", "c1")).fromClause(tableName("other")).build()))));
    }
}
