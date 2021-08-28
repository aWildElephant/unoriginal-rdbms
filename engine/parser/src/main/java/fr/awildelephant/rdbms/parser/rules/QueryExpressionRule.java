package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.WithElement;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.With.with;
import static fr.awildelephant.rdbms.ast.WithElement.withElement;
import static fr.awildelephant.rdbms.ast.WithList.withList;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.*;
import static fr.awildelephant.rdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.rdbms.parser.rules.ExplicitTableRule.deriveExplicitTableRule;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.*;
import static fr.awildelephant.rdbms.parser.rules.QuerySpecificationRule.deriveQuerySpecificationRule;
import static fr.awildelephant.rdbms.parser.rules.TableValueConstructorRule.deriveTableValueConstructorRule;

final class QueryExpressionRule {

    private QueryExpressionRule() {

    }

    static AST deriveQueryExpression(final Lexer lexer) {
        final Token nextToken = lexer.lookupNextToken();

        return switch (nextToken.type()) {
            case SELECT -> deriveQuerySpecificationRule(lexer);
            case TABLE -> deriveExplicitTableRule(lexer);
            case VALUES -> deriveTableValueConstructorRule(lexer);
            case WITH -> deriveWithClauseFollowedByBody(lexer);
            default -> throw unexpectedToken(nextToken);
        };
    }

    private static AST deriveWithClauseFollowedByBody(Lexer lexer) {
        lexer.consumeNextToken();

        final List<WithElement> withElements = new ArrayList<>();

        do {
            final String withElementName = consumeIdentifier(lexer);

            consumeAndExpect(AS, lexer);
            consumeAndExpect(LEFT_PAREN, lexer);

            final AST withElementDefinition = deriveQueryExpression(lexer);

            consumeAndExpect(RIGHT_PAREN, lexer);

            withElements.add(withElement(withElementName, withElementDefinition));
        } while (consumeIfNextTokenIs(COMMA, lexer));

        final AST query = deriveQueryExpression(lexer);

        return with(withList(withElements), query);
    }
}
