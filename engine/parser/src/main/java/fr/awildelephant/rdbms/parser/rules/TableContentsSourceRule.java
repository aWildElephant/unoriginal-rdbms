package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.TableElementList;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.rdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.TableElementListRule.deriveTableElementList;

final class TableContentsSourceRule {

    private TableContentsSourceRule() {

    }

    static TableElementList deriveTableContentsSourceRule(final Lexer lexer) {
        if (nextTokenIs(LEFT_PAREN, lexer)) {
            return deriveTableElementList(lexer);
        } else {
            throw unexpectedToken(lexer.lookupNextToken());
        }
    }
}
