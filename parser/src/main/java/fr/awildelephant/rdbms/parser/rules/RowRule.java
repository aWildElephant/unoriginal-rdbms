package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.Row;
import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.LinkedList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.Row.row;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.*;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeInteger;

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
