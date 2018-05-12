package fr.awildelephant.gitrdbms.parser.fragments.infix;

import fr.awildelephant.gitrdbms.lexer.tokens.Token;
import fr.awildelephant.gitrdbms.parser.Parser;
import fr.awildelephant.gitrdbms.parser.ast.AST;
import fr.awildelephant.gitrdbms.parser.ast.ColumnDefinition;
import fr.awildelephant.gitrdbms.parser.ast.CreateTable;

import java.util.ArrayList;

import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.*;

public class CreateTableFragment implements InfixFragment {

    @Override
    public AST parse(Token token, Parser parser) {
        parser.consumeAndExpect(TABLE);
        final String tableName = parser.consumeAndExpect(IDENTIFIER).text();
        parser.consumeAndExpect(LEFT_PAREN);

        final ArrayList<ColumnDefinition> columns = new ArrayList<>();

        while (!parser.isNextTokenOFType(RIGHT_PAREN)) {
            columns.add(parseColumnDefinition(parser));

            if (parser.isNextTokenOFType(COMMA)) {
                parser.consumeAndExpect(COMMA);
            }
        }

        parser.consumeAndExpect(RIGHT_PAREN);

        return new CreateTable(tableName, columns);
    }

    private ColumnDefinition parseColumnDefinition(Parser parser) {
        final String columnName = parser.consumeAndExpect(IDENTIFIER).text();
        parser.consumeAndExpect(INTEGER);

        return new ColumnDefinition(columnName, ColumnDefinition.INTEGER);
    }
}
