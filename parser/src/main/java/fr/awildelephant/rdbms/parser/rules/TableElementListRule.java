package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.ColumnDefinition;
import fr.awildelephant.rdbms.ast.TableElementList;
import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.LinkedList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.TableElementList.tableElementList;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.*;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;

final class TableElementListRule {

    private TableElementListRule() {

    }

    static TableElementList deriveTableElementList(final Lexer lexer) {
        consumeAndExpect(lexer, LEFT_PAREN);

        final List<ColumnDefinition> elements = new LinkedList<>();

        elements.add(TableElement.deriveTableElement(lexer));

        while (lexer.lookupNextToken().type() == COMMA) {
            lexer.consumeNextToken();

            elements.add(TableElement.deriveTableElement(lexer));
        }

        consumeAndExpect(lexer, RIGHT_PAREN);

        return tableElementList(elements);
    }
}
