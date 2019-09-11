package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.ast.InnerJoin.innerJoin;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INNER;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.JOIN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.ON;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.rdbms.parser.rules.BooleanValueExpressionRule.deriveBooleanValueExpressionRule;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.QueryExpressionRule.deriveQueryExpression;
import static fr.awildelephant.rdbms.parser.rules.TableNameRule.deriveTableName;

final class TableReferenceRule {

    static AST deriveTableReferenceRule(Lexer lexer) {
        final AST leftTable = deriveTablePrimary(lexer);

        if (!nextTokenIs(INNER, lexer)) {
            return leftTable;
        }

        lexer.consumeNextToken();

        consumeAndExpect(JOIN, lexer);

        final AST rightTable = deriveTableReferenceRule(lexer);

        consumeAndExpect(ON, lexer);

        final AST joinSpecification = deriveBooleanValueExpressionRule(lexer);

        return innerJoin(leftTable, rightTable, joinSpecification);
    }

    private static AST deriveTablePrimary(Lexer lexer) {
        if (nextTokenIs(LEFT_PAREN, lexer)) {
            consumeAndExpect(LEFT_PAREN, lexer);

            final AST input = deriveQueryExpression(lexer);

            consumeAndExpect(RIGHT_PAREN, lexer);

            return input;
        }

        return deriveTableName(lexer);
    }
}
