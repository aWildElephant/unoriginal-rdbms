package fr.awildelephant.rdbms.parser;

import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.ast.value.CountStar;
import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.ast.Explain.explain;
import static fr.awildelephant.rdbms.ast.builder.SelectBuilder.select;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

public class ExplainParserTest {

    @Test
    void it_should_parse_an_explain_statement() {
        assertParsing("EXPLAIN SELECT COUNT(*) FROM t1",

                explain(select()
                        .outputColumns(CountStar.countStar())
                        .fromClause(TableName.tableName("t1")).build()));
    }
}
