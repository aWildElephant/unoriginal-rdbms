package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.ast.SortSpecificationList;
import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.SortSpecificationList.sortSpecificationList;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.rdbms.parser.rules.ColumnReferenceRule.deriveColumnReference;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;

final class SortSpecificationRule {

    private SortSpecificationRule() {

    }

    static SortSpecificationList deriveSortSpecification(final Lexer lexer) {
        final List<ColumnName> columns = new ArrayList<>();

        columns.add(deriveColumnReference(lexer));

        while (nextTokenIs(COMMA, lexer)) {
            lexer.consumeNextToken();

            columns.add(deriveColumnReference(lexer));
        }

        return sortSpecificationList(columns);
    }
}
