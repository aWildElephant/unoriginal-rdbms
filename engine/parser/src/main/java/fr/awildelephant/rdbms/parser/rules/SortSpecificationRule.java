package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.ast.SortSpecification;
import fr.awildelephant.rdbms.ast.ordering.NullsHandling;
import fr.awildelephant.rdbms.ast.ordering.OrderingSpecification;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import static fr.awildelephant.rdbms.ast.SortSpecification.sortSpecification;
import static fr.awildelephant.rdbms.ast.ordering.NullsHandling.NULLS_FIRST;
import static fr.awildelephant.rdbms.ast.ordering.NullsHandling.NULLS_LAST;
import static fr.awildelephant.rdbms.ast.ordering.NullsHandling.UNSPECIFIED;
import static fr.awildelephant.rdbms.ast.ordering.OrderingSpecification.ASCENDING;
import static fr.awildelephant.rdbms.ast.ordering.OrderingSpecification.DESCENDING;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.FIRST;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LAST;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.NULLS;
import static fr.awildelephant.rdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.rdbms.parser.rules.ColumnReferenceRule.deriveColumnReference;

final class SortSpecificationRule {

    private SortSpecificationRule() {

    }

    static SortSpecification deriveSortSpecification(Lexer lexer) {
        final ColumnName columnName = deriveColumnReference(lexer);

        final OrderingSpecification orderingSpecification = deriveOrderingSpecification(lexer);

        final NullsHandling nullsHandling = deriveNullsHandling(lexer);

        return sortSpecification(columnName, orderingSpecification, nullsHandling);
    }

    private static OrderingSpecification deriveOrderingSpecification(Lexer lexer) {
        switch (lexer.lookupNextToken().type()) {
            case ASC -> lexer.consumeNextToken();
            case DESC -> {
                lexer.consumeNextToken();
                return DESCENDING;
            }
        }

        return ASCENDING;
    }

    private static NullsHandling deriveNullsHandling(Lexer lexer) {
        NullsHandling nullsHandling = UNSPECIFIED;
        if (lexer.lookupNextToken().type() == NULLS) {
            lexer.consumeNextToken();

            final Token nullsOrdering = lexer.lookupNextToken();
            nullsHandling = switch (nullsOrdering.type()) {
                case FIRST -> NULLS_FIRST;
                case LAST -> NULLS_LAST;
                default -> throw unexpectedToken(nullsOrdering, FIRST, LAST);
            };
            lexer.consumeNextToken();
        }

        return nullsHandling;
    }
}
