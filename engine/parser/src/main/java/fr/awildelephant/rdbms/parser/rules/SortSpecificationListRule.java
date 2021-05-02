package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.SortSpecification;
import fr.awildelephant.rdbms.ast.SortSpecificationList;
import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.SortSpecificationList.sortSpecificationList;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIfNextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.SortSpecificationRule.deriveSortSpecification;

final class SortSpecificationListRule {

    private SortSpecificationListRule() {

    }

    static SortSpecificationList deriveSortSpecificationList(final Lexer lexer) {
        final List<SortSpecification> columns = new ArrayList<>();

        columns.add(deriveSortSpecification(lexer));

        while (consumeIfNextTokenIs(COMMA, lexer)) {
            columns.add(deriveSortSpecification(lexer));
        }

        return sortSpecificationList(columns);
    }
}
