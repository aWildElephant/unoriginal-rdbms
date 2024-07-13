package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.LinkedList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.Asterisk.asterisk;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.ASTERISK;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.rdbms.parser.rules.DerivedColumnRule.deriveDerivedColumn;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIfNextTokenIs;
import static java.util.Collections.singletonList;

final class SelectListRule {

    private SelectListRule() {

    }

    static List<AST> deriveSelectListRule(final Lexer lexer) {
        if (consumeIfNextTokenIs(ASTERISK, lexer)) {
            return singletonList(asterisk());
        }

        final List<AST> selectList = new LinkedList<>();

        do {
            selectList.add(deriveDerivedColumn(lexer));
        } while (consumeIfNextTokenIs(COMMA, lexer));

        return selectList;
    }
}
