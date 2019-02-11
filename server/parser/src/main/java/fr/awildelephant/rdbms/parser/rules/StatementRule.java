package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import static fr.awildelephant.rdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.rdbms.parser.rules.DropTableStatementRule.deriveDropTableStatement;
import static fr.awildelephant.rdbms.parser.rules.InsertStatementRule.deriveInsertStatementRule;
import static fr.awildelephant.rdbms.parser.rules.QuerySpecificationRule.deriveQuerySpecificationRule;
import static fr.awildelephant.rdbms.parser.rules.TableDefinitionRule.deriveTableDefinitionRule;
import static fr.awildelephant.rdbms.parser.rules.TableValueConstructorRule.deriveTableValueConstructorRule;

public final class StatementRule {

    private StatementRule() {

    }

    public static AST deriveStatementRule(final Lexer lexer) {
        final Token token = lexer.lookupNextToken();

        switch (token.type()) {
            case CREATE:
                return deriveTableDefinitionRule(lexer);
            case DROP:
                return deriveDropTableStatement(lexer);
            case INSERT:
                return deriveInsertStatementRule(lexer);
            case SELECT:
                return deriveQuerySpecificationRule(lexer);
            case VALUES:
                return deriveTableValueConstructorRule(lexer);
            default:
                throw unexpectedToken(token);
        }
    }
}
