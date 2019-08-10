package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.GroupingSetsList;
import fr.awildelephant.rdbms.ast.SortSpecificationList;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.IntegerLiteralToken;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Distinct.distinct;
import static fr.awildelephant.rdbms.ast.GroupBy.groupBy;
import static fr.awildelephant.rdbms.ast.Limit.limit;
import static fr.awildelephant.rdbms.ast.SortedSelect.sortedSelect;
import static fr.awildelephant.rdbms.ast.Where.where;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.BY;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DISTINCT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.GROUP;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INTEGER_LITERAL;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LIMIT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.ORDER;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.SELECT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.WHERE;
import static fr.awildelephant.rdbms.parser.rules.BooleanValueExpressionRule.deriveBooleanValueExpressionRule;
import static fr.awildelephant.rdbms.parser.rules.FromClauseRule.deriveFromClauseRule;
import static fr.awildelephant.rdbms.parser.rules.GroupingSpecificationRule.deriveGroupingSpecification;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.SelectListRule.deriveSelectListRule;
import static fr.awildelephant.rdbms.parser.rules.SortSpecificationListRule.deriveSortSpecificationList;

final class QuerySpecificationRule {

    private QuerySpecificationRule() {

    }

    static AST deriveQuerySpecificationRule(final Lexer lexer) {
        consumeAndExpect(SELECT, lexer);

        final boolean distinct = consumeIfDistinct(lexer);

        final List<AST> outputColumns = deriveSelectListRule(lexer);

        AST input = deriveFromClauseRule(lexer);

        if (nextTokenIs(WHERE, lexer)) {
            lexer.consumeNextToken();

            final AST filter = deriveBooleanValueExpressionRule(lexer);

            input = where(input, filter);
        }

        if (nextTokenIs(GROUP, lexer)) {
            lexer.consumeNextToken();

            consumeAndExpect(BY, lexer);

            final GroupingSetsList groupingSpecification = deriveGroupingSpecification(lexer);

            input = groupBy(input, groupingSpecification);
        }

        SortSpecificationList sortSpecificationList = null;

        if (nextTokenIs(ORDER, lexer)) {
            lexer.consumeNextToken();

            consumeAndExpect(BY, lexer);

            sortSpecificationList = deriveSortSpecificationList(lexer);
        }

        input = sortedSelect(outputColumns, sortSpecificationList, input);

        if (distinct) {
            input = distinct(input);
        }

        if (nextTokenIs(LIMIT, lexer)) {
            lexer.consumeNextToken();

            final int limit = ((IntegerLiteralToken) consumeAndExpect(INTEGER_LITERAL, lexer)).value();

            input = limit(input, limit);
        }

        return input;
    }

    private static boolean consumeIfDistinct(Lexer lexer) {
        if (nextTokenIs(DISTINCT, lexer)) {
            lexer.consumeNextToken();

            return true;
        }

        return false;
    }
}
