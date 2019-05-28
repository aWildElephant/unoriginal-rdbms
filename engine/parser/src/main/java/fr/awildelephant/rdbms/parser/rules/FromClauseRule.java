package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.FROM;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.SimpleTableRule.deriveSimpleTableRule;
import static fr.awildelephant.rdbms.parser.rules.TableNameRule.deriveTableName;

final class FromClauseRule {

    private FromClauseRule() {

    }

    static AST deriveFromClauseRule(final Lexer lexer) {
        consumeAndExpect(FROM, lexer);

        if (nextTokenIs(LEFT_PAREN, lexer)) {
            consumeAndExpect(LEFT_PAREN, lexer);

            final AST input = deriveSimpleTableRule(lexer);

            consumeAndExpect(RIGHT_PAREN, lexer);

            return input;
        }

        return deriveTableName(lexer);
    }
}
