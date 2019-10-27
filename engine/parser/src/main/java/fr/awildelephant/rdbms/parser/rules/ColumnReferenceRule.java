package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.IdentifierChain;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.ast.IdentifierChain.identifierChain;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;

final class ColumnReferenceRule {

    private ColumnReferenceRule() {

    }

    static IdentifierChain deriveColumnReference(Lexer lexer) {
        return identifierChain(consumeIdentifier(lexer));
    }
}
