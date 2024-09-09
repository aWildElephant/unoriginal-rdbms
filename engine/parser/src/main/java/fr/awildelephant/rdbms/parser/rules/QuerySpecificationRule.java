package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.GroupingSetsList;
import fr.awildelephant.rdbms.ast.SortSpecificationList;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.IntegerLiteralToken;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Distinct.distinct;
import static fr.awildelephant.rdbms.ast.Limit.limit;
import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.BY;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DISTINCT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.GROUP;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.HAVING;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INTEGER_LITERAL;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LIMIT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.ORDER;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.SELECT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.WHERE;
import static fr.awildelephant.rdbms.parser.rules.BooleanValueExpressionRule.deriveBooleanValueExpression;
import static fr.awildelephant.rdbms.parser.rules.FromClauseRule.deriveFromClauseRule;
import static fr.awildelephant.rdbms.parser.rules.GroupingSpecificationRule.deriveGroupingSpecification;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIfNextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.SelectListRule.deriveSelectListRule;
import static fr.awildelephant.rdbms.parser.rules.SortSpecificationListRule.deriveSortSpecificationList;

final class QuerySpecificationRule {

    private QuerySpecificationRule() {

    }

    static AST deriveQuerySpecificationRule(final Lexer lexer) {
        consumeAndExpect(SELECT, lexer);

        final boolean distinct = consumeIfNextTokenIs(DISTINCT, lexer);

        final List<AST> outputColumns = deriveSelectListRule(lexer);

        final AST fromClause = deriveFromClauseRule(lexer);

        final AST whereClause;
        if (consumeIfNextTokenIs(WHERE, lexer)) {
            whereClause = deriveBooleanValueExpression(lexer);
        } else {
            whereClause = null;
        }

        final GroupingSetsList groupByClause;
        if (consumeIfNextTokenIs(GROUP, lexer)) {
            consumeAndExpect(BY, lexer);

            groupByClause = deriveGroupingSpecification(lexer);
        } else {
            groupByClause = null;
        }

        final AST havingClause;
        if (consumeIfNextTokenIs(HAVING, lexer)) {
            havingClause = deriveBooleanValueExpression(lexer);
        } else {
            havingClause = null;
        }

        final SortSpecificationList orderByClause;
        if (consumeIfNextTokenIs(ORDER, lexer)) {
            consumeAndExpect(BY, lexer);

            orderByClause = deriveSortSpecificationList(lexer);
        } else {
            orderByClause = null;
        }

        AST output = select(outputColumns, fromClause, whereClause, groupByClause, havingClause, orderByClause);

        if (distinct) {
            output = distinct(output);
        }

        if (consumeIfNextTokenIs(LIMIT, lexer)) {
            final int limit = ((IntegerLiteralToken) consumeAndExpect(INTEGER_LITERAL, lexer)).value().intValue();

            output = limit(output, limit);
        }

        return output;
    }
}
