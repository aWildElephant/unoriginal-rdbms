package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Asterisk;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.IdentifierChain;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.stream.Stream;

public final class AsteriskExpander extends DefaultASTVisitor<Stream<AST>> {

    private final Schema inputSchema;

    AsteriskExpander(Schema inputSchema) {
        this.inputSchema = inputSchema;
    }

    @Override
    public Stream<AST> visit(Asterisk asterisk) {
        return inputSchema.columnNames()
                          .stream()
                          .map(IdentifierChain::identifierChain);
    }

    @Override
    public Stream<AST> defaultVisit(AST node) {
        return Stream.of(node);
    }
}
