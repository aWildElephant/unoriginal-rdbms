package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.With;
import fr.awildelephant.rdbms.ast.WithList;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;
import fr.awildelephant.rdbms.lexer.tokens.TokenType;

import java.util.List;

import static fr.awildelephant.rdbms.ast.With.with;
import static fr.awildelephant.rdbms.ast.WithElement.withElement;
import static fr.awildelephant.rdbms.ast.WithList.withList;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.AS;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.rdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.rdbms.parser.rules.ExplicitTableRule.deriveExplicitTableRule;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;
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
            case WITH:
                return deriveWithClauseFollowedByBody(lexer);
            default:
                throw unexpectedToken(nextToken);
        }
    }

    private static AST deriveWithClauseFollowedByBody(Lexer lexer) {
        lexer.consumeNextToken();

        final String withElementName = consumeIdentifier(lexer);

        consumeAndExpect(AS, lexer);
        consumeAndExpect(LEFT_PAREN, lexer);

        final AST withElementDefinition = deriveQueryExpression(lexer);

        consumeAndExpect(RIGHT_PAREN, lexer);

        final AST query = deriveQueryExpression(lexer);

        return with(withList(List.of(withElement(withElementName, withElementDefinition))), query);
    }
}
