package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;

public final class ColumnNameListRule {

    private ColumnNameListRule() {

    }

    public static List<String> deriveColumnNameList(Lexer lexer) {
        final List<String> columnNames = new ArrayList<>();
        columnNames.add(consumeIdentifier(lexer));

        while (nextTokenIs(COMMA, lexer)) {
            lexer.consumeNextToken();

            columnNames.add(consumeIdentifier(lexer));
        }

        return columnNames;
    }
}
