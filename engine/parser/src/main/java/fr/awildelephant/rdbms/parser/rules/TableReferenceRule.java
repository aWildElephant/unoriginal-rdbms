package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import java.util.List;

import static fr.awildelephant.rdbms.ast.InnerJoin.innerJoin;
import static fr.awildelephant.rdbms.ast.LeftJoin.leftJoin;
import static fr.awildelephant.rdbms.ast.TableAlias.tableAlias;
import static fr.awildelephant.rdbms.ast.TableAliasWithColumns.tableAliasWithColumns;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.AS;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.IDENTIFIER;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.JOIN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.ON;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.OUTER;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.VALUES;
import static fr.awildelephant.rdbms.parser.rules.BooleanValueExpressionRule.deriveBooleanValueExpression;
import static fr.awildelephant.rdbms.parser.rules.ColumnNameListRule.deriveColumnNameList;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIfNextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.QueryExpressionRule.deriveQueryExpression;
import static fr.awildelephant.rdbms.parser.rules.TableExpressionRule.deriveTableName;
import static fr.awildelephant.rdbms.parser.rules.TableValueConstructorRule.deriveTableValueConstructorRule;

final class TableReferenceRule {

    static AST deriveTableReferenceRule(Lexer lexer) {
        final AST leftInput = deriveTablePrimary(lexer);

        final Token nextToken = lexer.lookupNextToken();

        switch (nextToken.type()) {
            case INNER -> {
                lexer.consumeNextToken();
                consumeAndExpect(JOIN, lexer);
                final AST innerJoinRightInput = deriveTableReferenceRule(lexer);
                consumeAndExpect(ON, lexer);
                final AST innerJoinSpecification = deriveBooleanValueExpression(lexer);
                return innerJoin(leftInput, innerJoinRightInput, innerJoinSpecification);
            }
            case LEFT -> {
                lexer.consumeNextToken();
                consumeAndExpect(OUTER, lexer);
                consumeAndExpect(JOIN, lexer);
                final AST leftJoinRightInput = deriveTableReferenceRule(lexer);
                consumeAndExpect(ON, lexer);
                final AST leftJoinSpecification = deriveBooleanValueExpression(lexer);
                return leftJoin(leftInput, leftJoinRightInput, leftJoinSpecification);
            }
            default -> {
                return leftInput;
            }
        }
    }

    private static AST deriveTablePrimary(Lexer lexer) {
        final AST tablePrimary;
        if (consumeIfNextTokenIs(LEFT_PAREN, lexer)) {
            tablePrimary = deriveQueryExpression(lexer);

            consumeAndExpect(RIGHT_PAREN, lexer);
        } else if (nextTokenIs(VALUES, lexer)) {
            tablePrimary = deriveTableValueConstructorRule(lexer);
        } else {
            tablePrimary = deriveTableName(lexer);
        }

        consumeIfNextTokenIs(AS, lexer);

        if (!nextTokenIs(IDENTIFIER, lexer)) {
            return tablePrimary;
        }

        final String alias = consumeIdentifier(lexer);

        if (!nextTokenIs(LEFT_PAREN, lexer)) {
            return tableAlias(tablePrimary, alias);
        }

        lexer.consumeNextToken();

        List<String> columnAliases = deriveColumnNameList(lexer);

        consumeAndExpect(RIGHT_PAREN, lexer);

        return tableAliasWithColumns(tablePrimary, alias, columnAliases);
    }
}
