package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.ast.OrderingSpecification;
import fr.awildelephant.rdbms.ast.SortSpecification;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.ast.OrderingSpecification.ASCENDING;
import static fr.awildelephant.rdbms.ast.OrderingSpecification.DESCENDING;
import static fr.awildelephant.rdbms.ast.SortSpecification.sortSpecification;
import static fr.awildelephant.rdbms.parser.rules.ColumnReferenceRule.deriveColumnReference;

final class SortSpecificationRule {

    private SortSpecificationRule() {

    }

    static SortSpecification deriveSortSpecification(Lexer lexer) {
        final ColumnName columnName = deriveColumnReference(lexer);

        return sortSpecification(columnName, deriveOrderingSpecification(lexer));
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
}
