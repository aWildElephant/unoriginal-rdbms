package fr.awildelephant.rdbms.lexer.tokens;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.CHAR;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.CREATE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DATE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DECIMAL;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DISTINCT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DROP;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.FOREIGN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.FROM;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INSERT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INTEGER;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INTO;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.KEY;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.NOT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.NULL;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.REFERENCES;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.SELECT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.TABLE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.TEXT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.UNIQUE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.VALUES;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.VARCHAR;

public enum Keywords implements Token {
    CHAR_TOKEN {
        @Override
        public String text() {
            return "char";
        }

        @Override
        public TokenType type() {
            return CHAR;
        }
    },
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
    DATE_TOKEN {
        @Override
        public String text() {
            return "date";
        }

        @Override
        public TokenType type() {
            return DATE;
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
    },
    VARCHAR_TOKEN {
        @Override
        public String text() {
            return "varchar";
        }

        @Override
        public TokenType type() {
            return VARCHAR;
        }
    }
}
