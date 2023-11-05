package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.TokenType;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Function.function;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.TABLE;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.ValueExpressionRule.deriveTextLiteral;

final class TableExpressionRule {

    private TableExpressionRule() {

    }

    static TableName deriveTableName(Lexer lexer) {
        return tableName(consumeIdentifier(lexer));
    }

    static AST deriveTableNameOrFunction(Lexer lexer) {
        final String identifier = consumeIdentifier(lexer);

        if (nextTokenIs(TokenType.LEFT_PAREN, lexer)) {
            lexer.consumeNextToken();

            final AST singleParameter = deriveTextLiteral(lexer);

            consumeAndExpect(TokenType.RIGHT_PAREN, lexer);

            return function(identifier, List.of(singleParameter));
        }

        return tableName(identifier);
    }

    static AST deriveExplicitTableRule(final Lexer lexer) {
        consumeAndExpect(TABLE, lexer);

        return deriveTableName(lexer);
    }
}
