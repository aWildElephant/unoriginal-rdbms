package fr.awildelephant.gitrdbms.parser.rules;

import fr.awildelephant.gitrdbms.ast.Row;
import fr.awildelephant.gitrdbms.lexer.Lexer;

import java.util.LinkedList;
import java.util.List;

import static fr.awildelephant.gitrdbms.ast.Row.row;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.gitrdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.gitrdbms.parser.rules.ParseHelper.consumeInteger;

final class RowRule {

    private RowRule() {

    }

    static Row deriveRowRule(final Lexer lexer) {
        consumeAndExpect(lexer, LEFT_PAREN);

        final List<Integer> values = new LinkedList<>();
        values.add(consumeInteger(lexer));

        while (lexer.lookupNextToken().type() == COMMA) {
            lexer.consumeNextToken();

            values.add(consumeInteger(lexer));
        }

        consumeAndExpect(lexer, RIGHT_PAREN);

        return row(values);
    }
}
