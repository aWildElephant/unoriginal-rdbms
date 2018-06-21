package fr.awildelephant.rdbms.lexer.tokens;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.*;

public enum Keywords implements Token {
    CREATE_TOKEN {
        @Override
        public String text() {
            return "create";
        }

        @Override
        public TokenType type() {
            return CREATE;
        }
    },
    FROM_TOKEN {
        @Override
        public String text() {
            return "from";
        }

        @Override
        public TokenType type() {
            return FROM;
        }
    },
    INSERT_TOKEN {
        @Override
        public String text() {
            return "insert";
        }

        @Override
        public TokenType type() {
            return INSERT;
        }
    },
    INTEGER_TOKEN {
        @Override
        public String text() {
            return "integer";
        }

        @Override
        public TokenType type() {
            return INTEGER;
        }
    },
    INTO_TOKEN {
        @Override
        public String text() {
            return "into";
        }

        @Override
        public TokenType type() {
            return INTO;
        }
    },
    SELECT_TOKEN {
        @Override
        public String text() {
            return "select";
        }

        @Override
        public TokenType type() {
            return SELECT;
        }
    },
    TABLE_TOKEN {
        @Override
        public String text() {
            return "table";
        }

        @Override
        public TokenType type() {
            return TABLE;
        }
    },
    TEXT_TOKEN {
        @Override
        public String text() {
            return "text";
        }

        @Override
        public TokenType type() {
            return TEXT;
        }
    },
    VALUES_TOKEN {
        @Override
        public String text() {
            return "values";
        }

        @Override
        public TokenType type() {
            return VALUES;
        }
    }
}
