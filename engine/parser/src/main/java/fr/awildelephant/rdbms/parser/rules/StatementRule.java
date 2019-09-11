package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import static fr.awildelephant.rdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.rdbms.parser.rules.DropTableStatementRule.deriveDropTableStatement;
import static fr.awildelephant.rdbms.parser.rules.InsertStatementRule.deriveInsertStatementRule;
import static fr.awildelephant.rdbms.parser.rules.QueryExpressionRule.deriveQueryExpression;
import static fr.awildelephant.rdbms.parser.rules.TableDefinitionRule.deriveTableDefinitionRule;

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
            case TABLE:
            case VALUES:
                return deriveQueryExpression(lexer);
            default:
                throw unexpectedToken(token);
        }
    }
}
