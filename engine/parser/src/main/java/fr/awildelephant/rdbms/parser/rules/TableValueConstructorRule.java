package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.Row;
import fr.awildelephant.rdbms.ast.Values;
import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.ArrayList;

import static fr.awildelephant.rdbms.ast.Values.rows;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.VALUES;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIfNextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.RowRule.deriveRowRule;

final class TableValueConstructorRule {

    private TableValueConstructorRule() {

    }

    static Values deriveTableValueConstructorRule(final Lexer lexer) {
        consumeAndExpect(VALUES, lexer);

        final ArrayList<Row> rows = new ArrayList<>();
        rows.add(deriveRowRule(lexer));

        while (consumeIfNextTokenIs(COMMA, lexer)) {
            rows.add(deriveRowRule(lexer));
        }

        return rows(rows);
    }
}
