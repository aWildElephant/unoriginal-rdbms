package fr.awildelephant.gitrdbms.parser;

import fr.awildelephant.gitrdbms.lexer.Lexer;
import fr.awildelephant.gitrdbms.lexer.tokens.Token;
import fr.awildelephant.gitrdbms.lexer.tokens.TokenType;
import fr.awildelephant.gitrdbms.parser.ast.AST;
import fr.awildelephant.gitrdbms.parser.fragments.infix.CreateTableFragment;
import fr.awildelephant.gitrdbms.parser.fragments.infix.InfixFragment;

import java.util.EnumMap;
import java.util.InputMismatchException;
import java.util.Map;

import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.CREATE;

public final class Parser {

    private static final Map<TokenType, InfixFragment> infixFragments = new EnumMap<>(TokenType.class);

    static {
        infixFragments.put(CREATE, new CreateTableFragment());
    }

    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public AST parse() {
        final Token token = lexer.consumeNextToken();

        final InfixFragment fragment = infixFragments.get(token.type());

        if (fragment == null) {
            throw new UnsupportedOperationException("Unexpected token " + token.text());
        }

        return fragment.parse(token, this);
    }

    public Token consumeAndExpect(TokenType expectedType) {
        final Token token = lexer.consumeNextToken();

        if (token.type() != expectedType) {
            throw new InputMismatchException("Expected " + expectedType + " but got " + token.type());
        }

        return token;
    }
}
