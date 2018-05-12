package fr.awildelephant.gitrdbms.parser.fragments.infix;

import fr.awildelephant.gitrdbms.lexer.tokens.Token;
import fr.awildelephant.gitrdbms.parser.Parser;
import fr.awildelephant.gitrdbms.ast.AST;

public interface InfixFragment {

    AST parse(Token token, Parser parser);
}
