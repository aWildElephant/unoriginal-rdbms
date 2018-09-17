package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.GroupingSetsList.groupingSetsList;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.rdbms.parser.rules.ColumnReferenceRule.deriveColumnReference;

final class GroupingSpecificationRule {

    private GroupingSpecificationRule() {

    }

    static AST deriveGroupingSpecification(Lexer lexer) {
        final List<ColumnName> columns = new ArrayList<>();

        columns.add(deriveColumnReference(lexer));

        while (lexer.lookupNextToken().type() == COMMA) {
            lexer.consumeNextToken();

            columns.add(deriveColumnReference(lexer));
        }

        return groupingSetsList(columns);
    }
}
