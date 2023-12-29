package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.ast.ColumnAlias.columnAlias;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.AS;
import static fr.awildelephant.rdbms.parser.rules.BooleanValueExpressionRule.deriveBooleanValueExpression;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIfNextTokenIs;

final class DerivedColumnRule {

    private DerivedColumnRule() {

    }

    static AST deriveDerivedColumn(final Lexer lexer) {
        final AST expression = deriveBooleanValueExpression(lexer);

        if (consumeIfNextTokenIs(AS, lexer)) {
            final String alias = consumeIdentifier(lexer);

            return columnAlias(expression, alias);
        }

        return expression;
    }
}
