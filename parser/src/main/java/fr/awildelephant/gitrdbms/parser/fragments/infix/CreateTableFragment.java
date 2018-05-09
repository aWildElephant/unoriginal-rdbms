package fr.awildelephant.gitrdbms.parser.fragments.infix;

import fr.awildelephant.gitrdbms.lexer.tokens.Token;
import fr.awildelephant.gitrdbms.parser.Parser;
import fr.awildelephant.gitrdbms.parser.ast.AST;
import fr.awildelephant.gitrdbms.parser.ast.CreateTable;

import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.IDENTIFIER;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.TABLE;

public class CreateTableFragment implements InfixFragment {

    @Override
    public AST parse(Token token, Parser parser) {
        parser.consumeAndExpect(TABLE);
        final String tableName = parser.consumeAndExpect(IDENTIFIER).text();
        parser.consumeAndExpect(LEFT_PAREN);
        parser.consumeAndExpect(RIGHT_PAREN);

        return new CreateTable(tableName);
    }
}
