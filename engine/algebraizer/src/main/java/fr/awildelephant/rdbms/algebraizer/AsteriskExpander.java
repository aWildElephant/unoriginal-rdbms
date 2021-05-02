package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Asterisk;
import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.ast.visitor.DefaultASTVisitor;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.stream.Stream;

import static fr.awildelephant.rdbms.ast.QualifiedColumnName.qualifiedColumnName;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;

public final class AsteriskExpander extends DefaultASTVisitor<Stream<AST>> {

    private final Schema inputSchema;

    AsteriskExpander(Schema inputSchema) {
        this.inputSchema = inputSchema;
    }

    @Override
    public Stream<AST> visit(Asterisk asterisk) {
        return inputSchema.columnNames()
                          .stream()
                          .filter(columnReference -> !inputSchema.column(columnReference).system())
                          .map(columnName -> columnName.table()
                                  .<ColumnName>map(qualifier -> qualifiedColumnName(qualifier, columnName.name()))
                                  .orElseGet(() -> unqualifiedColumnName(columnName.name())));
    }

    @Override
    public Stream<AST> defaultVisit(AST node) {
        return Stream.of(node);
    }
}
