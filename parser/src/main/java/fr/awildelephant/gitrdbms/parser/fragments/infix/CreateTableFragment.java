package fr.awildelephant.gitrdbms.parser.fragments.infix;

import fr.awildelephant.gitrdbms.lexer.tokens.Token;
import fr.awildelephant.gitrdbms.parser.Parser;
import fr.awildelephant.gitrdbms.parser.ast.AST;
import fr.awildelephant.gitrdbms.parser.ast.ColumnDefinition;
import fr.awildelephant.gitrdbms.parser.ast.CreateTable;

import java.util.ArrayList;
import java.util.Collections;

import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.IDENTIFIER;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.INTEGER;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.TABLE;

public class CreateTableFragment implements InfixFragment {

    @Override
    public AST parse(Token token, Parser parser) {
        parser.consumeAndExpect(TABLE);
        final String tableName = parser.consumeAndExpect(IDENTIFIER).text();
        parser.consumeAndExpect(LEFT_PAREN);

        if (parser.isNextTokenOFType(RIGHT_PAREN)) {
            parser.consumeAndExpect(RIGHT_PAREN);

            return new CreateTable(tableName, Collections.emptyList());
        } else {
            final String columnName = parser.consumeAndExpect(IDENTIFIER).text();
            parser.consumeAndExpect(INTEGER);
            parser.consumeAndExpect(RIGHT_PAREN);

            final ArrayList<ColumnDefinition> columns = new ArrayList<>();
            columns.add(new ColumnDefinition(columnName, ColumnDefinition.INTEGER));

            return new CreateTable(tableName, columns);
        }
    }
}
