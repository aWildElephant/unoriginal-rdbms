package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.value.BooleanLiteral;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import static fr.awildelephant.rdbms.ast.value.And.and;
import static fr.awildelephant.rdbms.ast.value.Not.not;
import static fr.awildelephant.rdbms.ast.value.Or.or;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.AND;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.NOT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.OR;
import static fr.awildelephant.rdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.rdbms.parser.rules.ColumnReferenceRule.deriveColumnReference;

final class BooleanValueExpressionRule {

    private BooleanValueExpressionRule() {

    }

    static AST deriveBooleanValueExpressionRule(final Lexer lexer) {
        final AST left = deriveTerm(lexer);

        if (lexer.lookupNextToken().type() == OR) {
            lexer.consumeNextToken();

            return or(left, deriveBooleanValueExpressionRule(lexer));
        }

        return left;
    }

    private static AST deriveTerm(final Lexer lexer) {
        final AST left = deriveFactor(lexer);

        if (lexer.lookupNextToken().type() == AND) {
            lexer.consumeNextToken();

            return and(left, deriveTerm(lexer));
        }

        return left;
    }

    private static AST deriveFactor(final Lexer lexer) {
        if (lexer.lookupNextToken().type() == NOT) {
            lexer.consumeNextToken();

            return not(deriveBooleanTest(lexer));
        }

        return deriveBooleanTest(lexer);
    }

    private static AST deriveBooleanTest(final Lexer lexer) {
        final Token nextToken = lexer.lookupNextToken();

        switch (nextToken.type()) {
            case TRUE:
                lexer.consumeNextToken();

                return BooleanLiteral.TRUE;
            case FALSE:
                lexer.consumeNextToken();

                return BooleanLiteral.FALSE;
            case IDENTIFIER:
                return deriveColumnReference(lexer);
            default:
                throw unexpectedToken(nextToken);
        }
    }
}
