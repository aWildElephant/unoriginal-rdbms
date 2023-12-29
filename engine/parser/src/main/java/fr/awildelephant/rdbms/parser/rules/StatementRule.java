package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.END_OF_FILE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.SEMICOLON;
import static fr.awildelephant.rdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.rdbms.parser.rules.DeleteStatementRule.deriveDeleteStatement;
import static fr.awildelephant.rdbms.parser.rules.DropTableStatementRule.deriveDropTableStatement;
import static fr.awildelephant.rdbms.parser.rules.ExplainStatementRule.deriveExplainStatement;
import static fr.awildelephant.rdbms.parser.rules.InsertStatementRule.deriveInsertStatement;
import static fr.awildelephant.rdbms.parser.rules.QueryExpressionRule.deriveQueryExpression;
import static fr.awildelephant.rdbms.parser.rules.TableDefinitionRule.deriveCreateStatement;
import static fr.awildelephant.rdbms.parser.rules.TruncateStatementRule.deriveTruncateStatement;

public final class StatementRule {

    private StatementRule() {

    }

    public static AST deriveStatementRule(final Lexer lexer) {
        final AST statement = parseStatement(lexer);

        final Token lastToken = lexer.lookupNextToken();
        if (lastToken.type() != END_OF_FILE && lastToken.type() != SEMICOLON) {
            throw unexpectedToken(lastToken);
        }

        return statement;
    }

    private static AST parseStatement(final Lexer lexer) {
        final Token token = lexer.lookupNextToken();

        return switch (token.type()) {
            case CREATE -> deriveCreateStatement(lexer);
            case DELETE -> deriveDeleteStatement(lexer);
            case DROP -> deriveDropTableStatement(lexer);
            case EXPLAIN -> deriveExplainStatement(lexer);
            case INSERT -> deriveInsertStatement(lexer);
            case TRUNCATE -> deriveTruncateStatement(lexer);
            case READ, SELECT, TABLE, VALUES, WITH -> deriveQueryExpression(lexer);
            default -> throw unexpectedToken(token);
        };
    }
}
