package fr.awildelephant.gitrdbms.lexer;

import fr.awildelephant.gitrdbms.lexer.tokens.IdentifierToken;
import fr.awildelephant.gitrdbms.lexer.tokens.IntegerLiteralToken;
import org.junit.jupiter.api.Test;

import static fr.awildelephant.gitrdbms.lexer.LexingTestHelper.assertLexing;
import static fr.awildelephant.gitrdbms.lexer.tokens.StaticToken.*;

class LexerTest {

    @Test
    void it_should_ignore_a_space() {
        assertLexing("CREATE TABLE", CREATE_TOKEN, TABLE_TOKEN);
    }

    @Test
    void it_should_tokenize_a_create_table_statement() {
        assertLexing("CREATE TABLE x (y INTEGER, z INTEGER);",

                CREATE_TOKEN,
                TABLE_TOKEN,
                new IdentifierToken("x"),
                LEFT_PAREN_TOKEN,
                new IdentifierToken("y"),
                INTEGER_TOKEN,
                COMMA_TOKEN,
                new IdentifierToken("z"),
                INTEGER_TOKEN,
                RIGHT_PAREN_TOKEN,
                SEMICOLON_TOKEN);
    }

    @Test
    void it_should_tokenize_an_insert_into_statement() {
        assertLexing("INSERT INTO x (y) VALUES (1);",

                INSERT_TOKEN,
                INTO_TOKEN,
                new IdentifierToken("x"),
                LEFT_PAREN_TOKEN,
                new IdentifierToken("y"),
                RIGHT_PAREN_TOKEN,
                VALUES_TOKEN,
                LEFT_PAREN_TOKEN,
                new IntegerLiteralToken(1),
                RIGHT_PAREN_TOKEN,
                SEMICOLON_TOKEN);
    }
}
