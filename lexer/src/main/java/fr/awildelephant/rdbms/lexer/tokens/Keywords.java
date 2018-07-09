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
    DECIMAL_TOKEN {
        @Override
        public String text() {
            return "decimal";
        }

        @Override
        public TokenType type() {
            return DECIMAL;
        }
    },
    DISTINCT_TOKEN {
        @Override
        public String text() {
            return "distinct";
        }

        @Override
        public TokenType type() {
            return DISTINCT;
        }
    },
    DROP_TABLE_TOKEN {
        @Override
        public String text() {
            return "drop";
        }

        @Override
        public TokenType type() {
            return DROP;
        }
    },
    FOREIGN_TOKEN {
        @Override
        public String text() {
            return "foreign";
        }

        @Override
        public TokenType type() {
            return FOREIGN;
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
    KEY_TOKEN {
        @Override
        public String text() {
            return "key";
        }

        @Override
        public TokenType type() {
            return KEY;
        }
    },
    NOT_TOKEN {
        @Override
        public String text() {
            return "not";
        }

        @Override
        public TokenType type() {
            return NOT;
        }
    },
    NULL_TOKEN {
        @Override
        public String text() {
            return "null";
        }

        @Override
        public TokenType type() {
            return NULL;
        }
    },
    REFERENCES_TOKEN {
        @Override
        public String text() {
            return "references";
        }

        @Override
        public TokenType type() {
            return REFERENCES;
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
    UNIQUE_TOKEN {
        @Override
        public String text() {
            return "unique";
        }

        @Override
        public TokenType type() {
            return UNIQUE;
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
