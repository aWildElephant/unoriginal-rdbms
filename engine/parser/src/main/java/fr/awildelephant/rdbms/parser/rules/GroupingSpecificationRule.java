package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.ast.GroupingSetsList;
import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.GroupingSetsList.groupingSetsList;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.rdbms.parser.rules.ColumnReferenceRule.deriveColumnReference;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIfNextTokenIs;

final class GroupingSpecificationRule {

    private GroupingSpecificationRule() {

    }

    static GroupingSetsList deriveGroupingSpecification(Lexer lexer) {
        final List<ColumnName> columns = new ArrayList<>();

        do {
            columns.add(deriveColumnReference(lexer));
        } while (consumeIfNextTokenIs(COMMA, lexer));

        return groupingSetsList(columns);
    }
}
