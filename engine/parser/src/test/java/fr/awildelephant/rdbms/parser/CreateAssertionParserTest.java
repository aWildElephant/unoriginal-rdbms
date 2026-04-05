package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.builder.CreateAssertionBuilder.createAssertion;
import static fr.awildelephant.rdbms.ast.builder.OperationBuilder.operation;
import static fr.awildelephant.rdbms.ast.builder.SelectBuilder.select;
import static fr.awildelephant.rdbms.ast.value.CountStar.countStar;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class CreateAssertionParserTest {

    @Test
    void it_should_parse_a_create_assertion_statement() {
        assertParsing("CREATE ASSERTION check_count CHECK ((SELECT COUNT(*) FROM t1) <= 1)",

                createAssertion("check_count")
                        .check(operation(select().outputColumns(countStar()).fromClause(tableName("t1")))
                                .isLowerThanOrEqualTo(1)));
    }
}
