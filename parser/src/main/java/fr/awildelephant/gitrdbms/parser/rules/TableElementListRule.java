package fr.awildelephant.gitrdbms.parser.rules;

import fr.awildelephant.gitrdbms.ast.ColumnDefinition;
import fr.awildelephant.gitrdbms.ast.TableElementList;
import fr.awildelephant.gitrdbms.lexer.Lexer;

import java.util.LinkedList;
import java.util.List;

import static fr.awildelephant.gitrdbms.ast.TableElementList.tableElementList;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.*;
import static fr.awildelephant.gitrdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.gitrdbms.parser.rules.TableElement.deriveTableElement;

final class TableElementListRule {

    private TableElementListRule() {

    }

    static TableElementList deriveTableElementList(final Lexer lexer) {
        consumeAndExpect(lexer, LEFT_PAREN);

        final List<ColumnDefinition> elements = new LinkedList<>();

        elements.add(deriveTableElement(lexer));

        while (lexer.lookupNextToken().type() == COMMA) {
            lexer.consumeNextToken();

            elements.add(deriveTableElement(lexer));
        }

        consumeAndExpect(lexer, RIGHT_PAREN);

        return tableElementList(elements);
    }
}
