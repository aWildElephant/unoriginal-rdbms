package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.Rows;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.ast.Rows.rows;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.VALUES;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static java.util.Collections.singletonList;

final class FromConstructorRule {

    private FromConstructorRule() {

    }

    static Rows deriveFromConstructorRule(final Lexer lexer) {
        consumeAndExpect(lexer, VALUES);

        return rows(singletonList(RowRule.deriveRowRule(lexer)));
    }
}
