package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.IdentifierChain;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.ast.IdentifierChain.identifierChain;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.PERIOD;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;

final class ColumnReferenceRule {

    private ColumnReferenceRule() {

    }

    static IdentifierChain deriveColumnReference(Lexer lexer) {
        final String firstIdentifier = consumeIdentifier(lexer);

        if (!nextTokenIs(PERIOD, lexer)) {
            return identifierChain(firstIdentifier);
        }

        final IdentifierChain.IdentifierChainBuilder builder = IdentifierChain.builder();

        builder.add(firstIdentifier);

        do {
            lexer.consumeNextToken();

            builder.add(consumeIdentifier(lexer));
        } while (nextTokenIs(PERIOD, lexer));

        return builder.build();
    }
}
