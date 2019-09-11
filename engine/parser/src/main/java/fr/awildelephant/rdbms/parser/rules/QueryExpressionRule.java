package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import static fr.awildelephant.rdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.rdbms.parser.rules.ExplicitTableRule.deriveExplicitTableRule;
import static fr.awildelephant.rdbms.parser.rules.QuerySpecificationRule.deriveQuerySpecificationRule;
import static fr.awildelephant.rdbms.parser.rules.TableValueConstructorRule.deriveTableValueConstructorRule;

final class QueryExpressionRule {

    private QueryExpressionRule() {

    }

    static AST deriveQueryExpression(final Lexer lexer) {
        final Token nextToken = lexer.lookupNextToken();

        switch (nextToken.type()) {
            case SELECT:
                return deriveQuerySpecificationRule(lexer);
            case TABLE:
                return deriveExplicitTableRule(lexer);
            case VALUES:
                return deriveTableValueConstructorRule(lexer);
            default:
                throw unexpectedToken(nextToken);
        }
    }
}
