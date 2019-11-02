package fr.awildelephant.rdbms.lexer.tokens;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.AND;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.AS;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.ASC;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.AVG;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.BETWEEN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.BOOLEAN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.BY;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.CAST;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.CHAR;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COUNT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.CREATE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DATE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DAY;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DECIMAL;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DESC;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DISTINCT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DROP;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.EXTRACT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.FALSE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.FOREIGN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.FROM;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.GROUP;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INNER;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INSERT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INTEGER;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INTERVAL;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INTO;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.JOIN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.KEY;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LIKE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LIMIT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.MIN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.NOT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.NULL;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.ON;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.OR;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.ORDER;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.REFERENCES;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.SELECT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.SUM;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.TABLE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.TEXT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.TRUE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.UNIQUE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.UNKNOWN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.VALUES;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.VARCHAR;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.WHERE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.YEAR;

public enum Keywords implements Token {

    AND_TOKEN {
        @Override
        public String text() {
            return "and";
        }

        @Override
        public TokenType type() {
            return AND;
        }
    },
    AS_TOKEN {
        @Override
        public String text() {
            return "as";
        }

        @Override
        public TokenType type() {
            return AS;
        }
    },
    ASC_TOKEN {
        @Override
        public String text() {
            return "asc";
        }

        @Override
        public TokenType type() {
            return ASC;
        }
    },
    AVG_TOKEN {
        @Override
        public String text() {
            return "avg";
        }

        @Override
        public TokenType type() {
            return AVG;
        }
    },
    BETWEEN_TOKEN {
        @Override
        public String text() {
            return "between";
        }

        @Override
        public TokenType type() {
            return BETWEEN;
        }
    },
    BOOLEAN_TOKEN {
        @Override
        public String text() {
            return "boolean";
        }

        @Override
        public TokenType type() {
            return BOOLEAN;
        }
    },
    BY_TOKEN {
        @Override
        public String text() {
            return "by";
        }

        @Override
        public TokenType type() {
            return BY;
        }
    },
    CAST_TOKEN {
        @Override
        public String text() {
            return "cast";
        }

        @Override
        public TokenType type() {
            return CAST;
        }
    },
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
    COUNT_TOKEN {
        @Override
        public String text() {
            return "count";
        }

        @Override
        public TokenType type() {
            return COUNT;
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
    DAY_TOKEN {
        @Override
        public String text() {
            return "day";
        }

        @Override
        public TokenType type() {
            return DAY;
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
    DESC_TOKEN {
        @Override
        public String text() {
            return "desc";
        }

        @Override
        public TokenType type() {
            return DESC;
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
    EXTRACT_TOKEN {
        @Override
        public String text() {
            return "extract";
        }

        @Override
        public TokenType type() {
            return EXTRACT;
        }
    },
    FALSE_TOKEN {
        @Override
        public String text() {
            return "false";
        }

        @Override
        public TokenType type() {
            return FALSE;
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
    GROUP_TOKEN {
        @Override
        public String text() {
            return "group";
        }

        @Override
        public TokenType type() {
            return GROUP;
        }
    },
    INNER_TOKEN {
        @Override
        public String text() {
            return "inner";
        }

        @Override
        public TokenType type() {
            return INNER;
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
    INTERVAL_TOKEN {
        @Override
        public String text() {
            return "interval";
        }

        @Override
        public TokenType type() {
            return INTERVAL;
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
    JOIN_TOKEN {
        @Override
        public String text() {
            return "join";
        }

        @Override
        public TokenType type() {
            return JOIN;
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
    LIKE_TOKEN {
        @Override
        public String text() {
            return "like";
        }

        @Override
        public TokenType type() {
            return LIKE;
        }
    },
    LIMIT_TOKEN {
        @Override
        public String text() {
            return "limit";
        }

        @Override
        public TokenType type() {
            return LIMIT;
        }
    },
    MIN_TOKEN {
        @Override
        public String text() {
            return "min";
        }

        @Override
        public TokenType type() {
            return MIN;
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
    ON_TOKEN {
        @Override
        public String text() {
            return "on";
        }

        @Override
        public TokenType type() {
            return ON;
        }
    },
    OR_TOKEN {
        @Override
        public String text() {
            return "or";
        }

        @Override
        public TokenType type() {
            return OR;
        }
    },
    ORDER_TOKEN {
        @Override
        public String text() {
            return "order";
        }

        @Override
        public TokenType type() {
            return ORDER;
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
    SUM_TOKEN {
        @Override
        public String text() {
            return "sum";
        }

        @Override
        public TokenType type() {
            return SUM;
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
    TRUE_TOKEN {
        @Override
        public String text() {
            return "true";
        }

        @Override
        public TokenType type() {
            return TRUE;
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
    UNKNOWN_TOKEN {
        @Override
        public String text() {
            return "unknown";
        }

        @Override
        public TokenType type() {
            return UNKNOWN;
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
    },
    WHERE_TOKEN {
        @Override
        public String text() {
            return "where";
        }

        @Override
        public TokenType type() {
            return WHERE;
        }
    },
    YEAR_TOKEN {
        @Override
        public String text() {
            return "year";
        }

        @Override
        public TokenType type() {
            return YEAR;
        }
    }
}
