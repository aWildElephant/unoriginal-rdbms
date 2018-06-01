package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;
import fr.awildelephant.rdbms.parser.error.ErrorHelper;

import static fr.awildelephant.rdbms.parser.rules.InsertStatementRule.deriveInsertStatementRule;

public final class StatementRule {

    private StatementRule() {

    }

    public static AST deriveStatementRule(final Lexer lexer) {
        final Token token = lexer.lookupNextToken();

        switch (token.type()) {
            case CREATE:
                return TableDefinitionRule.deriveTableDefinitionRule(lexer);
            case INSERT:
                return deriveInsertStatementRule(lexer);
            case SELECT:
                return QuerySpecificationRule.deriveQuerySpecificationRule(lexer);
            default:
                throw ErrorHelper.unexpectedToken(token);
        }
    }
}
