package fr.awildelephant.rdbms.lexer;

import fr.awildelephant.rdbms.lexer.exception.LexingException;
import fr.awildelephant.rdbms.lexer.tokens.IdentifierToken;
import fr.awildelephant.rdbms.lexer.tokens.IntegerLiteralToken;
import fr.awildelephant.rdbms.lexer.tokens.TextLiteralToken;
import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.lexer.LexingTestHelper.assertLexing;
import static fr.awildelephant.rdbms.lexer.LexingTestHelper.lexerFrom;
import static fr.awildelephant.rdbms.lexer.tokens.Keywords.COUNT_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.Keywords.CREATE_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.Keywords.DAY_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.Keywords.FALSE_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.Keywords.FROM_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.Keywords.INSERT_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.Keywords.INTEGER_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.Keywords.INTERVAL_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.Keywords.INTO_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.Keywords.SELECT_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.Keywords.TABLE_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.Keywords.TEXT_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.Keywords.TRUE_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.Keywords.VALUES_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.ASTERISK_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.COMMA_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.EQUAL_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.LEFT_PAREN_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.LESS_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.PERIOD_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.RIGHT_PAREN_TOKEN;
import static fr.awildelephant.rdbms.lexer.tokens.StaticToken.SEMICOLON_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LexerTest {

    @Test
    void it_should_ignore_a_space() {
        assertLexing("CREATE TABLE", CREATE_TOKEN, TABLE_TOKEN);
    }

    @Test
    void it_should_tokenize_a_create_table_statement() {
        assertLexing("CREATE TABLE x (y INTEGER, z TEXT);",

                CREATE_TOKEN,
                TABLE_TOKEN,
                new IdentifierToken("x"),
                LEFT_PAREN_TOKEN,
                new IdentifierToken("y"),
                INTEGER_TOKEN,
                COMMA_TOKEN,
                new IdentifierToken("z"),
                TEXT_TOKEN,
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

    @Test
    void it_should_tokenize_a_select_count_star() {
        assertLexing("SELECT COUNT(*) FROM test",

                SELECT_TOKEN,
                COUNT_TOKEN,
                LEFT_PAREN_TOKEN,
                ASTERISK_TOKEN,
                RIGHT_PAREN_TOKEN,
                FROM_TOKEN,
                new IdentifierToken("test"));
    }

    @Test
    void it_should_tokenize_boolean_literals() {
        assertLexing("SELECT TRUE, FALSE",

                SELECT_TOKEN,
                TRUE_TOKEN,
                COMMA_TOKEN,
                FALSE_TOKEN);
    }

    @Test
    void it_should_tokenize_an_interval_literal() {
        assertLexing("INTERVAL '120' DAY (3)",

                INTERVAL_TOKEN,
                new TextLiteralToken("120"),
                DAY_TOKEN,
                LEFT_PAREN_TOKEN,
                new IntegerLiteralToken(3),
                RIGHT_PAREN_TOKEN);
    }

    @Test
    void it_should_tokenize_an_identifier_chain() {
        assertLexing("one.two.three",

                new IdentifierToken("one"),
                PERIOD_TOKEN,
                new IdentifierToken("two"),
                PERIOD_TOKEN,
                new IdentifierToken("three"));
    }

    @Test
    void it_should_be_able_to_save_its_state_and_go_back_to_it_when_needed() {
        final Lexer lexer = new Lexer(InputStreamWrapper.wrap("SELECT * FROM test"));

        lexer.consumeNextToken();

        final LexerSnapshot snapshot = lexer.save();

        lexer.consumeNextToken();
        lexer.consumeNextToken();

        lexer.restore(snapshot);

        assertThat(lexer.lookupNextToken()).isEqualTo(ASTERISK_TOKEN);
    }

    @Test
    void it_should_not_tokenize_less_than_and_equal_as_less_than_or_equal_to() {
        assertLexing("< =", LESS_TOKEN, EQUAL_TOKEN);
    }

    @Test
    void it_should_properly_handle_accents_in_text_literals() {
        assertLexing("'àéïôù'", new TextLiteralToken("àéïôù"));
    }

    @Test
    void it_should_throw_an_exception_for_an_invalid_identifier() {
        final Lexer lexer = lexerFrom("2befree");

        assertThrows(LexingException.class, lexer::lookupNextToken);
    }
}
