package fr.awildelephant.rdbms.lexer;

import fr.awildelephant.rdbms.lexer.tokens.IdentifierToken;
import fr.awildelephant.rdbms.lexer.tokens.IntegerLiteralToken;
import fr.awildelephant.rdbms.lexer.tokens.StaticToken;
import org.junit.jupiter.api.Test;

class LexerTest {

    @Test
    void it_should_ignore_a_space() {
        LexingTestHelper.assertLexing("CREATE TABLE", StaticToken.CREATE_TOKEN, StaticToken.TABLE_TOKEN);
    }

    @Test
    void it_should_tokenize_a_create_table_statement() {
        LexingTestHelper.assertLexing("CREATE TABLE x (y INTEGER, z INTEGER);",

                StaticToken.CREATE_TOKEN,
                StaticToken.TABLE_TOKEN,
                new IdentifierToken("x"),
                StaticToken.LEFT_PAREN_TOKEN,
                new IdentifierToken("y"),
                StaticToken.INTEGER_TOKEN,
                StaticToken.COMMA_TOKEN,
                new IdentifierToken("z"),
                StaticToken.INTEGER_TOKEN,
                StaticToken.RIGHT_PAREN_TOKEN,
                StaticToken.SEMICOLON_TOKEN);
    }

    @Test
    void it_should_tokenize_an_insert_into_statement() {
        LexingTestHelper.assertLexing("INSERT INTO x (y) VALUES (1);",

                StaticToken.INSERT_TOKEN,
                StaticToken.INTO_TOKEN,
                new IdentifierToken("x"),
                StaticToken.LEFT_PAREN_TOKEN,
                new IdentifierToken("y"),
                StaticToken.RIGHT_PAREN_TOKEN,
                StaticToken.VALUES_TOKEN,
                StaticToken.LEFT_PAREN_TOKEN,
                new IntegerLiteralToken(1),
                StaticToken.RIGHT_PAREN_TOKEN,
                StaticToken.SEMICOLON_TOKEN);
    }
}
