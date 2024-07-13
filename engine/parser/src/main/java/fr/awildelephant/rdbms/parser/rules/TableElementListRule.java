package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.TableElementList;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.ast.TableElementList.tableElementList;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIfNextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.TableElementRule.deriveTableElement;

final class TableElementListRule {

    private TableElementListRule() {

    }

    static TableElementList deriveTableElementList(final Lexer lexer) {
        final TableElementList.Builder builder = tableElementList();

        consumeAndExpect(LEFT_PAREN, lexer);

        do {
            deriveTableElement(builder, lexer);
        } while (consumeIfNextTokenIs(COMMA, lexer));

        consumeAndExpect(RIGHT_PAREN, lexer);

        return builder.build();
    }
}
