package fr.awildelephant.gitrdbms.lexer.tokens;

import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.CREATE;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.END_OF_FILE;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.INTEGER;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.SEMICOLON;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.TABLE;

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
    CREATE_TOKEN {
        @Override
        public String text() {
            return "CREATE";
        }

        @Override
        public TokenType type() {
            return CREATE;
        }
    },
    INTEGER_TOKEN {
        @Override
        public String text() {
            return "INTEGER";
        }

        @Override
        public TokenType type() {
            return INTEGER;
        }
    },
    TABLE_TOKEN {
        @Override
        public String text() {
            return "TABLE";
        }

        @Override
        public TokenType type() {
            return TABLE;
        }
    }
}
