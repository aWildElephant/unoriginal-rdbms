package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.LinkedList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.Asterisk.asterisk;
import static fr.awildelephant.rdbms.ast.ColumnName.columnName;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.ASTERISK;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;
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

        final LinkedList<AST> selectList = new LinkedList<>();

        selectList.add(columnName(consumeIdentifier(lexer)));

        while (lexer.lookupNextToken().type() == COMMA) {
            lexer.consumeNextToken();

            selectList.add(columnName(consumeIdentifier(lexer)));
        }

        return selectList;
    }
}
