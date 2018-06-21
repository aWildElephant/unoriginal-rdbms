package fr.awildelephant.rdbms.lexer.tokens;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.*;

public enum StaticToken implements Token {

    END_OF_FILE_TOKEN {
        @Override
        public String text() {
            return "<end_of_file>";
        }

        @Override
        public TokenType type() {
            return END_OF_FILE;
        }
    },
    ASTERISK_TOKEN {
        @Override
        public String text() {
            return "*";
        }

        @Override
        public TokenType type() {
            return ASTERISK;
        }
    },
    COMMA_TOKEN {
        @Override
        public String text() {
            return ",";
        }

        @Override
        public TokenType type() {
            return COMMA;
        }
    },
    LEFT_PAREN_TOKEN {
        @Override
        public String text() {
            return "(";
        }

        @Override
        public TokenType type() {
            return LEFT_PAREN;
        }
    },
    RIGHT_PAREN_TOKEN {
        @Override
        public String text() {
            return ")";
        }

        @Override
        public TokenType type() {
            return RIGHT_PAREN;
        }
    },
    SEMICOLON_TOKEN {
        @Override
        public String text() {
            return ";";
        }

        @Override
        public TokenType type() {
            return SEMICOLON;
        }
    },
}
