package fr.awildelephant.gitrdbms.lexer.tokens;

import org.junit.jupiter.api.Test;

import static fr.awildelephant.gitrdbms.lexer.tokens.LexingTestHelper.assertLexing;
import static fr.awildelephant.gitrdbms.lexer.tokens.StaticToken.COMMA_TOKEN;
import static fr.awildelephant.gitrdbms.lexer.tokens.StaticToken.CREATE_TOKEN;
import static fr.awildelephant.gitrdbms.lexer.tokens.StaticToken.INTEGER_TOKEN;
import static fr.awildelephant.gitrdbms.lexer.tokens.StaticToken.LEFT_PAREN_TOKEN;
import static fr.awildelephant.gitrdbms.lexer.tokens.StaticToken.RIGHT_PAREN_TOKEN;
import static fr.awildelephant.gitrdbms.lexer.tokens.StaticToken.SEMICOLON_TOKEN;
import static fr.awildelephant.gitrdbms.lexer.tokens.StaticToken.TABLE_TOKEN;

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
}
