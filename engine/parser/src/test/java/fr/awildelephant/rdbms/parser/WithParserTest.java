package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Asterisk.asterisk;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.TableReferenceList.tableReferenceList;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.With.with;
import static fr.awildelephant.rdbms.ast.WithElement.withElement;
import static fr.awildelephant.rdbms.ast.WithList.withList;
import static fr.awildelephant.rdbms.ast.builder.SelectBuilder.select;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

// TODO: add WithBuilder
public class WithParserTest {

    @Test
    void it_should_parse_a_with_statement_with_a_single_element() {
        assertParsing("WITH with_query AS (SELECT a, b FROM t1) SELECT * FROM with_query",
                with(withList(List.of(withElement("with_query", select().outputColumns(unqualifiedColumnName("a"), unqualifiedColumnName("b")).fromClause(tableName("t1")).build()))),
                        select().outputColumns(asterisk())
                                .fromClause(tableName("with_query")).build()));
    }

    @Test
    void it_should_parse_a_with_statement_with_several_elements() {
        assertParsing("WITH first_clause AS (select * from t1), second_clause AS (select * from t2), third_clause AS (select * from t3) select * from t1, t2, t3",
                with(withList(List.of(withElement("first_clause", select()
                                        .outputColumns(asterisk())
                                        .fromClause(tableName("t1")).build()),
                                withElement("second_clause", select()
                                        .outputColumns(asterisk())
                                        .fromClause(tableName("t2")).build()),
                                withElement("third_clause", select()
                                        .outputColumns(asterisk())
                                        .fromClause(tableName("t3")).build()))),
                        select()
                                .outputColumns(asterisk())
                                .fromClause(tableReferenceList(tableName("t1"), tableName("t2"), List.of(tableName("t3")))).build()));
    }
}
