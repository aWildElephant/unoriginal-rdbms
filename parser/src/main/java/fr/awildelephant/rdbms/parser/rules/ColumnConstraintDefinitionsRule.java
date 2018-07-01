package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.ColumnDefinition;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.NULL;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;

final class ColumnConstraintDefinitionsRule {

    private ColumnConstraintDefinitionsRule() {

    }

    static void deriveColumnConstraintDefinitions(ColumnDefinition.Builder builder, Lexer lexer) {
        while (true) {
            final Token token = lexer.lookupNextToken();
            switch (token.type()) {
                case NOT:
                    lexer.consumeNextToken();
                    consumeAndExpect(NULL, lexer);

                    builder.notNull();
                    break;
                case UNIQUE:
                    lexer.consumeNextToken();

                    builder.unique();
                    break;
                default:
                    return;
            }
        }
    }
}
