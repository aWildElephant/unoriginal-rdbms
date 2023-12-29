package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Delete;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.ast.Delete.delete;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.FROM;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.WHERE;
import static fr.awildelephant.rdbms.parser.rules.BooleanValueExpressionRule.deriveBooleanValueExpression;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.TableExpressionRule.deriveTableName;

final class DeleteStatementRule {

    private DeleteStatementRule() {

    }

    static Delete deriveDeleteStatement(final Lexer lexer) {
        lexer.consumeNextToken(); // DELETE

        consumeAndExpect(FROM, lexer);

        final TableName tableName = deriveTableName(lexer);

        consumeAndExpect(WHERE, lexer);

        final AST predicate = deriveBooleanValueExpression(lexer);

        return delete(tableName, predicate);
    }
}
