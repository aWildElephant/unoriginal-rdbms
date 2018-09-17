package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.TableElementList;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.rdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.rdbms.parser.rules.TableElementListRule.deriveTableElementList;

final class TableContentsSourceRule {

    private TableContentsSourceRule() {

    }

    static TableElementList deriveTableContentsSourceRule(final Lexer lexer) {
        final Token token = lexer.lookupNextToken();

        if (token.type() == LEFT_PAREN) {
            return deriveTableElementList(lexer);
        } else {
            throw unexpectedToken(token);
        }
    }
}
