package fr.awildelephant.rdbms.lexer.tokens;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.AND;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.ANY;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.AS;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.ASC;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.AVG;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.BETWEEN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.BIGINT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.BOOLEAN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.BY;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.CASE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.CAST;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.CHAR;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COALESCE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COUNT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.CREATE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.CSV;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DATE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DAY;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DECIMAL;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DELETE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DESC;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DISTINCT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DROP;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.ELSE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.END;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.EXISTS;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.EXPLAIN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.EXTRACT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.FALSE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.FIRST;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.FOR;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.FOREIGN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.FROM;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.GROUP;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.HAVING;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.IN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INNER;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INSERT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INTEGER;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INTERVAL;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INTO;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.IS;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.JOIN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.KEY;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LAST;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LEFT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LIKE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LIMIT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.MAX;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.MIN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.MONTH;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.NOT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.NULL;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.NULLS;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.ON;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.OR;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.ORDER;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.OUTER;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.READ;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.REFERENCES;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.SELECT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.SUBSTRING;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.SUM;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.TABLE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.TEXT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.THEN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.TRUE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.TRUNCATE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.UNIQUE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.UNKNOWN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.VALUES;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.VARCHAR;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.VIEW;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.WHEN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.WHERE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.WITH;
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
    ANY_TOKEN {
        @Override
        public String text() {
            return "any";
        }

        @Override
        public TokenType type() {
            return ANY;
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
    BIGINT_TOKEN {
        @Override
        public String text() {
            return "bigint";
        }

        @Override
        public TokenType type() {
            return BIGINT;
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
    CASE_TOKEN {
        @Override
        public String text() {
            return "case";
        }

        @Override
        public TokenType type() {
            return CASE;
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
    COALESCE_TOKEN {
        @Override
        public String text() {
            return "coalesce";
        }

        @Override
        public TokenType type() {
            return COALESCE;
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
    CSV_TOKEN {
        @Override
        public String text() {
            return "csv";
        }

        @Override
        public TokenType type() {
            return CSV;
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
    DELETE_TOKEN {
        @Override
        public String text() {
            return "delete";
        }

        @Override
        public TokenType type() {
            return DELETE;
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
    ELSE_TOKEN {
        @Override
        public String text() {
            return "else";
        }

        @Override
        public TokenType type() {
            return ELSE;
        }
    },
    END_TOKEN {
        @Override
        public String text() {
            return "end";
        }

        @Override
        public TokenType type() {
            return END;
        }
    },
    EXISTS_TOKEN {
        @Override
        public String text() {
            return "exists";
        }

        @Override
        public TokenType type() {
            return EXISTS;
        }
    },
    EXPLAIN_TOKEN {
        @Override
        public String text() {
            return "explain";
        }

        @Override
        public TokenType type() {
            return EXPLAIN;
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
    FIRST_TOKEN {
        @Override
        public String text() {
            return "first";
        }

        @Override
        public TokenType type() {
            return FIRST;
        }
    },
    FOR_TOKEN {
        @Override
        public String text() {
            return "for";
        }

        @Override
        public TokenType type() {
            return FOR;
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
    HAVING_TOKEN {
        @Override
        public String text() {
            return "having";
        }

        @Override
        public TokenType type() {
            return HAVING;
        }
    },
    IN_TOKEN {
        @Override
        public String text() {
            return "in";
        }

        @Override
        public TokenType type() {
            return IN;
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
    IS_TOKEN {
        @Override
        public String text() {
            return "is";
        }

        @Override
        public TokenType type() {
            return IS;
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
    LAST_TOKEN {
        @Override
        public String text() {
            return "last";
        }

        @Override
        public TokenType type() {
            return LAST;
        }
    },
    LEFT_TOKEN {
        @Override
        public String text() {
            return "left";
        }

        @Override
        public TokenType type() {
            return LEFT;
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
    MAX_TOKEN {
        @Override
        public String text() {
            return "max";
        }

        @Override
        public TokenType type() {
            return MAX;
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
    MONTH_TOKEN {
        @Override
        public String text() {
            return "month";
        }

        @Override
        public TokenType type() {
            return MONTH;
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
    NULLS_TOKEN {
        @Override
        public String text() {
            return "nulls";
        }

        @Override
        public TokenType type() {
            return NULLS;
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
    OUTER_TOKEN {
        @Override
        public String text() {
            return "outer";
        }

        @Override
        public TokenType type() {
            return OUTER;
        }
    },
    READ_TOKEN {
        @Override
        public String text() {
            return "read";
        }

        @Override
        public TokenType type() {
            return READ;
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
    SUBSTRING_TOKEN {
        @Override
        public String text() {
            return "substring";
        }

        @Override
        public TokenType type() {
            return SUBSTRING;
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
    THEN_TOKEN {
        @Override
        public String text() {
            return "then";
        }

        @Override
        public TokenType type() {
            return THEN;
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
    TRUNCATE_TOKEN {
        @Override
        public String text() {
            return "truncate";
        }

        @Override
        public TokenType type() {
            return TRUNCATE;
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
    VIEW_TOKEN {
        @Override
        public String text() {
            return "view";
        }

        @Override
        public TokenType type() {
            return VIEW;
        }
    },
    WITH_TOKEN {
        @Override
        public String text() {
            return "with";
        }

        @Override
        public TokenType type() {
            return WITH;
        }
    },
    WHEN_TOKEN {
        @Override
        public String text() {
            return "when";
        }

        @Override
        public TokenType type() {
            return WHEN;
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
