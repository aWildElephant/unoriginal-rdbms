package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.ast.Cast.cast;
import static fr.awildelephant.rdbms.ast.ColumnDefinition.DATE;
import static fr.awildelephant.rdbms.ast.Row.row;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.Values.rows;
import static fr.awildelephant.rdbms.ast.value.And.and;
import static fr.awildelephant.rdbms.ast.value.Between.between;
import static fr.awildelephant.rdbms.ast.value.BooleanLiteral.FALSE;
import static fr.awildelephant.rdbms.ast.value.BooleanLiteral.TRUE;
import static fr.awildelephant.rdbms.ast.value.Equal.equal;
import static fr.awildelephant.rdbms.ast.value.Greater.greater;
import static fr.awildelephant.rdbms.ast.value.GreaterOrEqual.greaterOrEqual;
import static fr.awildelephant.rdbms.ast.value.IntegerLiteral.integerLiteral;
import static fr.awildelephant.rdbms.ast.value.Less.less;
import static fr.awildelephant.rdbms.ast.value.LessOrEqual.lessOrEqual;
import static fr.awildelephant.rdbms.ast.value.Like.like;
import static fr.awildelephant.rdbms.ast.value.Not.not;
import static fr.awildelephant.rdbms.ast.value.Or.or;
import static fr.awildelephant.rdbms.ast.value.Plus.plus;
import static fr.awildelephant.rdbms.ast.value.TextLiteral.textLiteral;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class BooleanExpressionParserTest {

    @Test
    void it_should_parse_an_and_expression() {
        assertParsing("VALUES (TRUE AND TRUE)", rows(row(and(TRUE, TRUE))));
    }

    @Test
    void it_should_parse_an_or_expression() {
        assertParsing("VALUES (FALSE OR TRUE)", rows(row(or(FALSE, TRUE))));
    }

    @Test
    void it_should_parse_a_not_expression() {
        assertParsing("VALUES (NOT TRUE)", rows(row(not(TRUE))));
    }

    @Test
    void it_should_parse_an_equal_comparison() {
        // TODO: I didn't put ê/à because the lexer doesn't handle them properly
        assertParsing("VALUES ((0 + 0) = 'la tete a toto')",
                      rows(row(equal(plus(integerLiteral(0), integerLiteral(0)), textLiteral("la tete a toto")))));
    }

    @Test
    void it_should_parse_a_less_than_or_equal_to_comparison() {
        assertParsing("VALUES(birthday <= date '2000-01-01')",
                      rows(row(lessOrEqual(unqualifiedColumnName("birthday"),
                                           cast(textLiteral("2000-01-01"), DATE)))));
    }

    @Test
    void it_should_parse_a_greater_than_or_equal_to_comparison() {
        assertParsing("VALUES(birthday >= date '2000-01-01')",

                      rows(row(greaterOrEqual(unqualifiedColumnName("birthday"),
                                              cast(textLiteral("2000-01-01"), DATE)))));
    }

    @Test
    void it_should_parse_a_less_than_comparison() {
        assertParsing("VALUES (a < 42)",
                      rows(row(less(unqualifiedColumnName("a"), integerLiteral(42)))));
    }

    @Test
    void it_should_parse_a_greater_than_comparison() {
        assertParsing("VALUES (a > 9000)",
                      rows(row(greater(unqualifiedColumnName("a"), integerLiteral(9000)))));
    }

    @Test
    void it_should_parse_a_comparison_between_two_columns_followed_by_another_comparison() {
        assertParsing("VALUES (a = b AND c = 0)",
                      rows(row(and(equal(unqualifiedColumnName("a"), unqualifiedColumnName("b")),
                                   equal(unqualifiedColumnName("c"), integerLiteral(0))))));
    }

    @Test
    void it_should_parse_a_like_filter() {
        assertParsing("VALUES (a LIKE '.boub%')",

                      rows(row(like(unqualifiedColumnName("a"), textLiteral(".boub%")))));
    }

    @Test
    void it_should_parse_a_not_like_filter() {
        assertParsing("VALUES (a NOT LIKE '.boub%')",

                      rows(row(not(like(unqualifiedColumnName("a"), textLiteral(".boub%"))))));
    }

    @Test
    void it_should_parse_a_boolean_filter() {
        assertParsing("VALUES (a BETWEEN 0 AND 99)",

                      rows(row(between(unqualifiedColumnName("a"), integerLiteral(0), integerLiteral(99)))));
    }
}
