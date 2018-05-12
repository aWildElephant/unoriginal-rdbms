package fr.awildelephant.gitrdbms.parser.rules;

import fr.awildelephant.gitrdbms.ast.AST;
import fr.awildelephant.gitrdbms.lexer.Lexer;

import java.util.LinkedList;
import java.util.List;

import static fr.awildelephant.gitrdbms.ast.ColumnName.columnName;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.gitrdbms.parser.rules.ParseHelper.consumeIdentifier;

final class SelectListRule {

    private SelectListRule() {

    }

    static List<AST> deriveSelectListRule(final Lexer lexer) {
        final LinkedList<AST> selectList = new LinkedList<>();

        selectList.add(columnName(consumeIdentifier(lexer)));

        while (lexer.lookupNextToken().type() == COMMA) {
            lexer.consumeNextToken();

            selectList.add(columnName(consumeIdentifier(lexer)));
        }

        return selectList;
    }
}
