package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.Row;
import fr.awildelephant.rdbms.ast.Values;
import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.Values.rows;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.VALUES;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIfNextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.RowRule.deriveRowRule;

final class TableValueConstructorRule {

    private TableValueConstructorRule() {

    }

    static Values deriveTableValueConstructorRule(final Lexer lexer) {
        consumeAndExpect(VALUES, lexer);

        if (!nextTokenIs(LEFT_PAREN, lexer)) {
            return rows(List.of());
        }

        final ArrayList<Row> rows = new ArrayList<>();

        do {
            rows.add(deriveRowRule(lexer));
        } while (consumeIfNextTokenIs(COMMA, lexer));

        return rows(rows);
    }
}
