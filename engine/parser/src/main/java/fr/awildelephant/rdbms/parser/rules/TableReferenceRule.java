package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.InnerJoin.innerJoin;
import static fr.awildelephant.rdbms.ast.LeftJoin.leftJoin;
import static fr.awildelephant.rdbms.ast.TableAlias.tableAlias;
import static fr.awildelephant.rdbms.ast.TableAliasWithColumns.tableAliasWithColumns;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.AS;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.IDENTIFIER;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.JOIN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.ON;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.OUTER;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.rdbms.parser.rules.BooleanValueExpressionRule.deriveBooleanValueExpressionRule;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.QueryExpressionRule.deriveQueryExpression;
import static fr.awildelephant.rdbms.parser.rules.TableNameRule.deriveTableName;

final class TableReferenceRule {

    static AST deriveTableReferenceRule(Lexer lexer) {
        final AST leftInput = deriveTablePrimary(lexer);

        final Token nextToken = lexer.lookupNextToken();

        switch (nextToken.type()) {
            case INNER:
                lexer.consumeNextToken();

                consumeAndExpect(JOIN, lexer);

                final AST innerJoinRightInput = deriveTableReferenceRule(lexer);

                consumeAndExpect(ON, lexer);

                final AST innerJoinSpecification = deriveBooleanValueExpressionRule(lexer);

                return innerJoin(leftInput, innerJoinRightInput, innerJoinSpecification);
            case LEFT:
                lexer.consumeNextToken();

                consumeAndExpect(OUTER, lexer);
                consumeAndExpect(JOIN, lexer);

                final AST leftJoinRightInput = deriveTableReferenceRule(lexer);

                consumeAndExpect(ON, lexer);

                final AST leftJoinSpecification = deriveBooleanValueExpressionRule(lexer);

                return leftJoin(leftInput, leftJoinRightInput, leftJoinSpecification);
            default:
                return leftInput;
        }
    }

    private static AST deriveTablePrimary(Lexer lexer) {
        final AST tablePrimary;
        if (nextTokenIs(LEFT_PAREN, lexer)) {
            lexer.consumeNextToken();

            tablePrimary = deriveQueryExpression(lexer);

            consumeAndExpect(RIGHT_PAREN, lexer);
        } else {
            tablePrimary = deriveTableName(lexer);
        }

        if (nextTokenIs(AS, lexer)) {
            lexer.consumeNextToken();
        }

        if (!nextTokenIs(IDENTIFIER, lexer)) {
            return tablePrimary;
        }

        final String alias = consumeIdentifier(lexer);

        if (!nextTokenIs(LEFT_PAREN, lexer)) {
            return tableAlias(tablePrimary, alias);
        }

        lexer.consumeNextToken();

        final List<String> columnAliases = new ArrayList<>();

        columnAliases.add(consumeIdentifier(lexer));

        while (nextTokenIs(COMMA, lexer)) {
            lexer.consumeNextToken();

            columnAliases.add(consumeIdentifier(lexer));
        }

        consumeAndExpect(RIGHT_PAREN, lexer);

        return tableAliasWithColumns(tablePrimary, alias, columnAliases);
    }
}
