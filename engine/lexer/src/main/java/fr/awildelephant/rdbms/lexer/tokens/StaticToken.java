package fr.awildelephant.rdbms.lexer.tokens;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.ASTERISK;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.END_OF_FILE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.EQUAL;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.GREATER;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LESS;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LESS_OR_EQUAL;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.MINUS;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.PLUS;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.SEMICOLON;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.SOLIDUS;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.WILDCARD;

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
    EQUAL_TOKEN {
        @Override
        public String text() {
            return "=";
        }

        @Override
        public TokenType type() {
            return EQUAL;
        }
    },
    GREATER_TOKEN {
        @Override
        public String text() {
            return ">";
        }

        @Override
        public TokenType type() {
            return GREATER;
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
    LESS_OR_EQUAL_TOKEN {
        @Override
        public String text() {
            return "<=";
        }

        @Override
        public TokenType type() {
            return LESS_OR_EQUAL;
        }
    },
    LESS_TOKEN {
        @Override
        public String text() {
            return "<";
        }

        @Override
        public TokenType type() {
            return LESS;
        }
    },
    MINUS_TOKEN {
        @Override
        public String text() {
            return "-";
        }

        @Override
        public TokenType type() {
            return MINUS;
        }
    },
    PLUS_TOKEN {
        @Override
        public String text() {
            return "+";
        }

        @Override
        public TokenType type() {
            return PLUS;
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
    SOLIDUS_TOKEN {
        @Override
        public String text() {
            return "/";
        }

        @Override
        public TokenType type() {
            return SOLIDUS;
        }
    },
    WILDCARD_TOKEN {
        @Override
        public String text() {
            return "?";
        }

        @Override
        public TokenType type() {
            return WILDCARD;
        }
    }
}
