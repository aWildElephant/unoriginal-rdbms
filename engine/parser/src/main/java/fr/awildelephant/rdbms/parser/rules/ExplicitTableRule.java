package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.TABLE;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.TableNameRule.deriveTableName;

final class ExplicitTableRule {

    private ExplicitTableRule() {

    }

    static AST deriveExplicitTableRule(final Lexer lexer) {
        consumeAndExpect(TABLE, lexer);

        return deriveTableName(lexer);
    }
}
