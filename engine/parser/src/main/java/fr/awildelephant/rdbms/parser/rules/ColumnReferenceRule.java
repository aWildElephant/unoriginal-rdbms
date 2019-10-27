package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.ColumnReference;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.ast.QualifiedColumnReference.qualifiedColumnReference;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnReference.unqualifiedColumnReference;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.PERIOD;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;

final class ColumnReferenceRule {

    private ColumnReferenceRule() {

    }

    static ColumnReference deriveColumnReference(Lexer lexer) {
        final String firstIdentifier = consumeIdentifier(lexer);

        if (!nextTokenIs(PERIOD, lexer)) {
            return unqualifiedColumnReference(firstIdentifier);
        }

        lexer.consumeNextToken();

        return qualifiedColumnReference(firstIdentifier, consumeIdentifier(lexer));
    }
}
