package fr.awildelephant.gitrdbms.lexer.tokens;

import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.*;

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
    FROM_TOKEN {
        @Override
        public String text() {
            return "FROM";
        }

        @Override
        public TokenType type() {
            return FROM;
        }
    },
    INSERT_TOKEN {
        @Override
        public String text() {
            return "INSERT";
        }

        @Override
        public TokenType type() {
            return INSERT;
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
    INTO_TOKEN {
        @Override
        public String text() {
            return "INTO";
        }

        @Override
        public TokenType type() {
            return INTO;
        }
    },
    SELECT_TOKEN {
        @Override
        public String text() {
            return "SELECT";
        }

        @Override
        public TokenType type() {
            return SELECT;
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
    },
    VALUES_TOKEN {
        @Override
        public String text() {
            return "VALUES";
        }

        @Override
        public TokenType type() {
            return VALUES;
        }
    }
}
