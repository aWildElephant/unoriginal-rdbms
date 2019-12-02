package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.END_OF_FILE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.SEMICOLON;
import static fr.awildelephant.rdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.rdbms.parser.rules.DropTableStatementRule.deriveDropTableStatement;
import static fr.awildelephant.rdbms.parser.rules.InsertStatementRule.deriveInsertStatementRule;
import static fr.awildelephant.rdbms.parser.rules.QueryExpressionRule.deriveQueryExpression;
import static fr.awildelephant.rdbms.parser.rules.TableDefinitionRule.deriveCreateStatement;

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

    private static AST parseStatement(Lexer lexer) {
        final Token token = lexer.lookupNextToken();

        switch (token.type()) {
            case CREATE:
                return deriveCreateStatement(lexer);
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
