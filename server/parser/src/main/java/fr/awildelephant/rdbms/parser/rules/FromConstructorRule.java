package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.Row;
import fr.awildelephant.rdbms.ast.Rows;
import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.ArrayList;

import static fr.awildelephant.rdbms.ast.Rows.rows;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.VALUES;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.RowRule.deriveRowRule;

final class FromConstructorRule {

    private FromConstructorRule() {

    }

    static Rows deriveFromConstructorRule(final Lexer lexer) {
        consumeAndExpect(VALUES, lexer);

        final ArrayList<Row> rows = new ArrayList<>();
        rows.add(deriveRowRule(lexer));

        while (lexer.lookupNextToken().type() == COMMA) {
            lexer.consumeNextToken();

            rows.add(deriveRowRule(lexer));
        }

        return rows(rows);
    }
}
