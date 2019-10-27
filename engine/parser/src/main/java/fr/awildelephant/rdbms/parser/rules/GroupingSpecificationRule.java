package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.ColumnReference;
import fr.awildelephant.rdbms.ast.GroupingSetsList;
import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.GroupingSetsList.groupingSetsList;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.rdbms.parser.rules.ColumnReferenceRule.deriveColumnReference;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;

final class GroupingSpecificationRule {

    private GroupingSpecificationRule() {

    }

    static GroupingSetsList deriveGroupingSpecification(Lexer lexer) {
        final List<ColumnReference> columns = new ArrayList<>();

        columns.add(deriveColumnReference(lexer));

        while (nextTokenIs(COMMA, lexer)) {
            lexer.consumeNextToken();

            columns.add(deriveColumnReference(lexer));
        }

        return groupingSetsList(columns);
    }
}
