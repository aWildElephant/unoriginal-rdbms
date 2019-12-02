package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.CreateView.createView;
import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class CreateViewParserTest {

    @Test
    void it_should_parse_a_view_definition() {
        assertParsing("CREATE VIEW my_view (my_a, my_b) AS SELECT a, b FROM test",

                      createView("my_view",
                                 List.of("my_a", "my_b"),
                                 select(List.of(unqualifiedColumnName("a"), unqualifiedColumnName("b")),
                                        tableName("test"),
                                        null,
                                        null,
                                        null,
                                        null)));
    }
}
