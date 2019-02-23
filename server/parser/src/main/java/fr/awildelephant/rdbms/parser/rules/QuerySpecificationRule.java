package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.GroupingSetsList;
import fr.awildelephant.rdbms.ast.Select;
import fr.awildelephant.rdbms.ast.SortSpecificationList;
import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Distinct.distinct;
import static fr.awildelephant.rdbms.ast.GroupBy.groupBy;
import static fr.awildelephant.rdbms.ast.OrderBy.orderBy;
import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.ast.Where.where;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.BY;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DISTINCT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.GROUP;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.ORDER;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.SELECT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.WHERE;
import static fr.awildelephant.rdbms.parser.rules.BooleanValueExpressionRule.deriveBooleanValueExpressionRule;
import static fr.awildelephant.rdbms.parser.rules.FromClauseRule.deriveFromClauseRule;
import static fr.awildelephant.rdbms.parser.rules.GroupingSpecificationRule.deriveGroupingSpecification;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.SelectListRule.deriveSelectListRule;
import static fr.awildelephant.rdbms.parser.rules.SortSpecificationRule.deriveSortSpecification;

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

        if (nextTokenIs(ORDER, lexer)) {
            lexer.consumeNextToken();

            consumeAndExpect(BY, lexer);

            final SortSpecificationList sortSpecificationList = deriveSortSpecification(lexer);

            input = orderBy(input, sortSpecificationList);
        }

        final Select select = select(outputColumns, input);

        if (distinct) {
            return distinct(select);
        }

        return select;
    }

    private static boolean consumeIfDistinct(Lexer lexer) {
        if (nextTokenIs(DISTINCT, lexer)) {
            lexer.consumeNextToken();

            return true;
        }

        return false;
    }
}
