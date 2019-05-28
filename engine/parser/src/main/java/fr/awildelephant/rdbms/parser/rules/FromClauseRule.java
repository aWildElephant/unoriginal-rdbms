package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.TableReferenceList.tableReferenceList;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.FROM;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;

final class FromClauseRule {

    private FromClauseRule() {

    }

    static AST deriveFromClauseRule(final Lexer lexer) {
        consumeAndExpect(FROM, lexer);

        final AST tableReference = TableReferenceRule.deriveTableReferenceRule(lexer);

        if (!nextTokenIs(COMMA, lexer)) {
            return tableReference;
        }

        final List<AST> tableReferences = new ArrayList<>();
        tableReferences.add(tableReference);

        do {
            lexer.consumeNextToken();

            tableReferences.add(TableReferenceRule.deriveTableReferenceRule(lexer));
        } while (nextTokenIs(COMMA, lexer));

        return tableReferenceList(tableReferences);
    }
}
