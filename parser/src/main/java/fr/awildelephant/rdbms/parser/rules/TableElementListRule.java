package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.ColumnDefinition;
import fr.awildelephant.rdbms.ast.TableElementList;
import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.LinkedList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.TableElementList.tableElementList;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.*;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.TableElement.deriveTableElement;

final class TableElementListRule {

    private TableElementListRule() {

    }

    static TableElementList deriveTableElementList(final Lexer lexer) {
        consumeAndExpect(LEFT_PAREN, lexer);

        final List<ColumnDefinition> elements = new LinkedList<>();

        elements.add(deriveTableElement(lexer));

        while (lexer.lookupNextToken().type() == COMMA) {
            lexer.consumeNextToken();

            elements.add(deriveTableElement(lexer));
        }

        consumeAndExpect(RIGHT_PAREN, lexer);

        return tableElementList(elements);
    }
}
