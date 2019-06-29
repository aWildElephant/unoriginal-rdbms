package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.LinkedList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.Asterisk.asterisk;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.ASTERISK;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.rdbms.parser.rules.DerivedColumnRule.deriveDerivedColumn;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;
import static java.util.Collections.singletonList;

final class SelectListRule {

    private SelectListRule() {

    }

    static List<AST> deriveSelectListRule(final Lexer lexer) {
        if (nextTokenIs(ASTERISK, lexer)) {
            lexer.consumeNextToken();

            return singletonList(asterisk());
        }

        final List<AST> selectList = new LinkedList<>();

        selectList.add(deriveDerivedColumn(lexer));

        while (nextTokenIs(COMMA, lexer)) {
            lexer.consumeNextToken();

            selectList.add(deriveDerivedColumn(lexer));
        }

        return selectList;
    }
}
