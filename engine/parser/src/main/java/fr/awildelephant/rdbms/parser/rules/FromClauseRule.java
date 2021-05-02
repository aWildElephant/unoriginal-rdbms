package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.TableReferenceList.tableReferenceList;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.FROM;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIfNextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.TableReferenceRule.deriveTableReferenceRule;

final class FromClauseRule {

    private FromClauseRule() {

    }

    static AST deriveFromClauseRule(final Lexer lexer) {
        consumeAndExpect(FROM, lexer);

        final AST tableReference = deriveTableReferenceRule(lexer);

        if (!nextTokenIs(COMMA, lexer)) {
            return tableReference;
        }

        lexer.consumeNextToken();

        final AST secondTableReference = deriveTableReferenceRule(lexer);

        if (!nextTokenIs(COMMA, lexer)) {
            return tableReferenceList(tableReference, secondTableReference, List.of());
        }

        final List<AST> additionalTableReferences = new ArrayList<>();

        while(consumeIfNextTokenIs(COMMA, lexer)) {
            additionalTableReferences.add(deriveTableReferenceRule(lexer));
        }

        return tableReferenceList(tableReference, secondTableReference, additionalTableReferences);
    }
}
