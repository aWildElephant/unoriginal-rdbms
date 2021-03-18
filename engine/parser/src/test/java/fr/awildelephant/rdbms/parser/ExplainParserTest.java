package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Explain.explain;
import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.value.CountStar.countStar;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

public class ExplainParserTest {

    @Test
    void it_should_parse_an_explain_statement() {
        assertParsing("EXPLAIN SELECT COUNT(*) FROM t1",

                      explain(select(List.of(countStar()), tableName("t1"), null, null, null, null)));
    }
}
