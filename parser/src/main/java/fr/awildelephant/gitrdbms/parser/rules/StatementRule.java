package fr.awildelephant.gitrdbms.parser.rules;

import fr.awildelephant.gitrdbms.ast.AST;
import fr.awildelephant.gitrdbms.lexer.Lexer;
import fr.awildelephant.gitrdbms.lexer.tokens.Token;

import static fr.awildelephant.gitrdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.gitrdbms.parser.rules.InsertStatementRule.deriveInsertStatementRule;
import static fr.awildelephant.gitrdbms.parser.rules.QuerySpecificationRule.deriveQuerySpecificationRule;
import static fr.awildelephant.gitrdbms.parser.rules.TableDefinitionRule.deriveTableDefinitionRule;

public final class StatementRule {

    private StatementRule() {

    }

    public static AST deriveStatementRule(final Lexer lexer) {
        final Token token = lexer.lookupNextToken();

        switch (token.type()) {
            case CREATE:
                return deriveTableDefinitionRule(lexer);
            case INSERT:
                return deriveInsertStatementRule(lexer);
            case SELECT:
                return deriveQuerySpecificationRule(lexer);
            default:
                throw unexpectedToken(token);
        }
    }
}
