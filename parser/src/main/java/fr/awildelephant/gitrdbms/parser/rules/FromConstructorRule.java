package fr.awildelephant.gitrdbms.parser.rules;

import fr.awildelephant.gitrdbms.ast.Rows;
import fr.awildelephant.gitrdbms.lexer.Lexer;

import static fr.awildelephant.gitrdbms.ast.Rows.rows;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.VALUES;
import static fr.awildelephant.gitrdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.gitrdbms.parser.rules.RowRule.deriveRowRule;
import static java.util.Collections.singletonList;

final class FromConstructorRule {

    private FromConstructorRule() {

    }

    static Rows deriveFromConstructorRule(final Lexer lexer) {
        consumeAndExpect(lexer, VALUES);

        return rows(singletonList(deriveRowRule(lexer)));
    }
}
