package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.ast.Truncate;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.ast.Truncate.truncate;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.TRUNCATE;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.TableExpressionRule.deriveTableName;

final class TruncateStatementRule {

    private TruncateStatementRule() {

    }

    public static Truncate deriveTruncateStatement(Lexer lexer) {
        consumeAndExpect(TRUNCATE, lexer);

        final TableName tableName = deriveTableName(lexer);

        return truncate(tableName);
    }
}
