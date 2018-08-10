package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.ast.ColumnAlias.columnAlias;
import static fr.awildelephant.rdbms.ast.ColumnName.columnName;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.AS;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;

final class DerivedColumnRule {

    private DerivedColumnRule() {

    }

    static AST deriveDerivedColumn(final Lexer lexer) {
        final ColumnName column = columnName(consumeIdentifier(lexer));

        if (nextTokenIs(AS, lexer)) {
            lexer.consumeNextToken();

            final String alias = consumeIdentifier(lexer);

            return columnAlias(column, alias);
        }

        return column;
    }
}
