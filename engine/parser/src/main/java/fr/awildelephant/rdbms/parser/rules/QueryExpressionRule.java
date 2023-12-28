package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnDefinition;
import fr.awildelephant.rdbms.ast.ReadCSV;
import fr.awildelephant.rdbms.ast.WithElement;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;
import fr.awildelephant.rdbms.lexer.tokens.TokenType;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.ReadCSV.readCSV;
import static fr.awildelephant.rdbms.ast.With.with;
import static fr.awildelephant.rdbms.ast.WithElement.withElement;
import static fr.awildelephant.rdbms.ast.WithList.withList;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.AS;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.CSV;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.rdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIfNextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.QuerySpecificationRule.deriveQuerySpecificationRule;
import static fr.awildelephant.rdbms.parser.rules.TableElementRule.deriveColumnDefinition;
import static fr.awildelephant.rdbms.parser.rules.TableExpressionRule.deriveExplicitTableRule;
import static fr.awildelephant.rdbms.parser.rules.TableValueConstructorRule.deriveTableValueConstructorRule;
import static fr.awildelephant.rdbms.parser.rules.ValueExpressionRule.deriveTextLiteral;

final class QueryExpressionRule {

    private QueryExpressionRule() {

    }

    static AST deriveQueryExpression(final Lexer lexer) {
        final Token nextToken = lexer.lookupNextToken();

        return switch (nextToken.type()) {
            case READ -> deriveReadCSVRule(lexer);
            case SELECT -> deriveQuerySpecificationRule(lexer);
            case TABLE -> deriveExplicitTableRule(lexer);
            case VALUES -> deriveTableValueConstructorRule(lexer);
            case WITH -> deriveWithClauseFollowedByBody(lexer);
            default -> throw unexpectedToken(nextToken);
        };
    }

    private static ReadCSV deriveReadCSVRule(final Lexer lexer) {
        lexer.consumeNextToken(); // READ

        consumeAndExpect(CSV, lexer);

        final String filePath = deriveTextLiteral(lexer).value();

        consumeAndExpect(LEFT_PAREN, lexer);

        final List<ColumnDefinition> columns;
        if (nextTokenIs(RIGHT_PAREN, lexer)) {
            lexer.consumeNextToken();

            columns = List.of();
        } else {
            columns = new ArrayList<>();
            columns.add(deriveColumnDefinition(lexer));

            while (nextTokenIs(COMMA, lexer)) {
                lexer.consumeNextToken();

                columns.add(deriveColumnDefinition(lexer));
            }

            consumeAndExpect(TokenType.RIGHT_PAREN, lexer);
        }

        return readCSV(filePath, columns);
    }

    private static AST deriveWithClauseFollowedByBody(Lexer lexer) {
        lexer.consumeNextToken();

        final List<WithElement> withElements = new ArrayList<>();

        do {
            final String withElementName = consumeIdentifier(lexer);

            consumeAndExpect(AS, lexer);
            consumeAndExpect(LEFT_PAREN, lexer);

            final AST withElementDefinition = deriveQueryExpression(lexer);

            consumeAndExpect(RIGHT_PAREN, lexer);

            withElements.add(withElement(withElementName, withElementDefinition));
        } while (consumeIfNextTokenIs(COMMA, lexer));

        final AST query = deriveQueryExpression(lexer);

        return with(withList(withElements), query);
    }
}
