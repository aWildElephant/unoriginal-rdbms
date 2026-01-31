package fr.awildelephant.rdbms.parser;

import fr.awildelephant.rdbms.ast.TableName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Coalesce.coalesce;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.builder.SelectBuilder.select;
import static fr.awildelephant.rdbms.ast.value.TextLiteral.textLiteral;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

public class FunctionParserTest {

    @Test
    void it_should_parse_coalesce_function_with_one_argument() {
        assertParsing("SELECT coalesce(columnA, 'default value') FROM t1",

                select()
                        .outputColumns(coalesce(List.of(unqualifiedColumnName("columna"), textLiteral("default value"))))
                        .fromClause(TableName.tableName("t1")));
    }
}
