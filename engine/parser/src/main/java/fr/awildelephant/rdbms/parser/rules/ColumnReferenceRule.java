package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.ast.QualifiedColumnName.qualifiedColumnName;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.PERIOD;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;

final class ColumnReferenceRule {

    private ColumnReferenceRule() {

    }

    static ColumnName deriveColumnReference(Lexer lexer) {
        final String firstIdentifier = consumeIdentifier(lexer);

        if (!nextTokenIs(PERIOD, lexer)) {
            return unqualifiedColumnName(firstIdentifier);
        }

        lexer.consumeNextToken();

        return qualifiedColumnName(firstIdentifier, consumeIdentifier(lexer));
    }
}
