package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.DropTable;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.ast.DropTable.dropTable;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DROP;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.TABLE;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.TableNameRule.deriveTableName;

final class DropTableStatementRule {

    private DropTableStatementRule() {

    }

    static DropTable deriveDropTableStatement(Lexer lexer) {
        consumeAndExpect(DROP, lexer);
        consumeAndExpect(TABLE, lexer);

        return dropTable(deriveTableName(lexer));
    }
}
