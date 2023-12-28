package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.TABLE;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;

final class TableExpressionRule {

    private TableExpressionRule() {

    }

    static TableName deriveTableName(Lexer lexer) {
        return tableName(consumeIdentifier(lexer));
    }

    static AST deriveExplicitTableRule(final Lexer lexer) {
        consumeAndExpect(TABLE, lexer);

        return deriveTableName(lexer);
    }
}
