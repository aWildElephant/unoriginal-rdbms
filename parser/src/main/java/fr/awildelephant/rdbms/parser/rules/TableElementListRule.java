package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.TableElementList;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.ast.TableElementList.tableElementList;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.*;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.TableElementRule.deriveTableElement;

final class TableElementListRule {

    private TableElementListRule() {

    }

    static TableElementList deriveTableElementList(final Lexer lexer) {
        final TableElementList.Builder builder = tableElementList();

        consumeAndExpect(LEFT_PAREN, lexer);

        deriveTableElement(builder, lexer);

        while (lexer.lookupNextToken().type() == COMMA) {
            lexer.consumeNextToken();

            deriveTableElement(builder, lexer);
        }

        consumeAndExpect(RIGHT_PAREN, lexer);

        return builder.build();
    }
}
