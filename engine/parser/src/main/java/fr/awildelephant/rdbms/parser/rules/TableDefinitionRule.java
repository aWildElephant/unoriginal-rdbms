package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.TableElementList;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;
import fr.awildelephant.rdbms.lexer.tokens.TokenType;

import java.util.List;

import static fr.awildelephant.rdbms.ast.CreateTable.createTable;
import static fr.awildelephant.rdbms.ast.CreateView.createView;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.AS;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.CREATE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.TABLE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.VIEW;
import static fr.awildelephant.rdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.rdbms.parser.rules.ColumnNameListRule.deriveColumnNameList;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;
import static fr.awildelephant.rdbms.parser.rules.QueryExpressionRule.deriveQueryExpression;
import static fr.awildelephant.rdbms.parser.rules.TableContentsSourceRule.deriveTableContentsSourceRule;
import static fr.awildelephant.rdbms.parser.rules.TableExpressionRule.deriveTableName;

final class TableDefinitionRule {

    private static final TokenType[] EXPECTED_AFTER_CREATE = new TokenType[]{TABLE, VIEW};

    private TableDefinitionRule() {

    }

    static AST deriveCreateStatement(final Lexer lexer) {
        consumeAndExpect(CREATE, lexer);

        final Token nextToken = lexer.lookupNextToken();
        switch (nextToken.type()) {
            case TABLE -> {
                lexer.consumeNextToken();
                final TableName tableName = deriveTableName(lexer);
                final TableElementList tableContentsSource = deriveTableContentsSourceRule(lexer);
                return createTable(tableName, tableContentsSource);
            }
            case VIEW -> {
                lexer.consumeNextToken();
                final String viewName = consumeIdentifier(lexer);
                consumeAndExpect(LEFT_PAREN, lexer);
                final List<String> columnNames = deriveColumnNameList(lexer);
                consumeAndExpect(RIGHT_PAREN, lexer);
                consumeAndExpect(AS, lexer);
                final AST query = deriveQueryExpression(lexer);
                return createView(viewName, columnNames, query);
            }
            default -> throw unexpectedToken(nextToken, EXPECTED_AFTER_CREATE);
        }
    }
}
