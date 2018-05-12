package fr.awildelephant.gitrdbms.parser.rules;

import fr.awildelephant.gitrdbms.ast.AST;
import fr.awildelephant.gitrdbms.lexer.Lexer;
import fr.awildelephant.gitrdbms.lexer.tokens.Token;

import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.gitrdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.gitrdbms.parser.rules.TableElementListRule.deriveTableElementList;

public final class TableContentsSourceRule {

    private TableContentsSourceRule() {

    }

    public static AST deriveTableContentsSourceRule(final Lexer lexer) {
        final Token token = lexer.lookupNextToken();

        if (token.type() == LEFT_PAREN) {
            return deriveTableElementList(lexer);
        } else {
            throw unexpectedToken(token);
        }
    }
}
