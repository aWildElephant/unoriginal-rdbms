package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.TokenType;

import static fr.awildelephant.rdbms.ast.ColumnName.columnName;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;

final class ValueExpressionRule {

    private ValueExpressionRule() {

    }

    static AST deriveValueExpression(final Lexer lexer) {
        final TokenType nextType = lexer.lookupNextToken().type();

        switch (nextType) {
            case LEFT_PAREN:
                return deriveParenthesizedValueExpression(lexer);
            default:
                return columnName(consumeIdentifier(lexer));
        }
    }

    private static AST deriveParenthesizedValueExpression(final Lexer lexer) {
        lexer.consumeNextToken();

        final AST expression = deriveValueExpression(lexer);

        consumeAndExpect(RIGHT_PAREN, lexer);

        return expression;
    }
}
