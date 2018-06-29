package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.Row;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.DecimalLiteralToken;
import fr.awildelephant.rdbms.lexer.tokens.IntegerLiteralToken;
import fr.awildelephant.rdbms.lexer.tokens.TextLiteralToken;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import java.util.LinkedList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.Row.row;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.*;
import static fr.awildelephant.rdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;

final class RowRule {

    private RowRule() {

    }

    static Row deriveRowRule(final Lexer lexer) {
        consumeAndExpect(LEFT_PAREN, lexer);

        final List<Object> values = new LinkedList<>();
        values.add(deriveValue(lexer));

        while (lexer.lookupNextToken().type() == COMMA) {
            lexer.consumeNextToken();

            values.add(deriveValue(lexer));
        }

        consumeAndExpect(RIGHT_PAREN, lexer);

        return row(values);
    }

    private static Object deriveValue(Lexer lexer) {
        final Token valueToken = lexer.consumeNextToken();

        switch (valueToken.type()) {
            case NULL:
                return null;
            case DECIMAL_LITERAL:
                return ((DecimalLiteralToken) valueToken).value();
            case INTEGER_LITERAL:
                return ((IntegerLiteralToken) valueToken).value();
            case TEXT_LITERAL:
                return ((TextLiteralToken) valueToken).content();
            default:
                throw unexpectedToken(valueToken);
        }
    }
}
