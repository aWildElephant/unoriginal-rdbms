package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Asterisk;
import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.ast.DecimalLiteral;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.IntegerLiteral;
import fr.awildelephant.rdbms.ast.NullLiteral;
import fr.awildelephant.rdbms.ast.TextLiteral;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.stream.Stream;

public class ColumnNameResolver extends DefaultASTVisitor<Stream<String>> {

    private final Schema inputSchema;

    ColumnNameResolver(Schema inputSchema) {
        this.inputSchema = inputSchema;
    }

    @Override
    public Stream<String> visit(Asterisk asterisk) {
        return inputSchema.columnNames().stream();
    }

    @Override
    public Stream<String> visit(ColumnName columnName) {
        final String name = columnName.name();

        if (!inputSchema.contains(name)) {
            throw new IllegalArgumentException("Column not found: " + name);
        }

        return Stream.of(name);
    }

    @Override
    public Stream<String> visit(IntegerLiteral integerLiteral) {
        return Stream.of(String.valueOf(integerLiteral.value()));
    }

    @Override
    public Stream<String> visit(DecimalLiteral decimalLiteral) {
        return Stream.of(decimalLiteral.value().toString());
    }

    @Override
    public Stream<String> visit(NullLiteral nullLiteral) {
        return Stream.of("null");
    }

    @Override
    public Stream<String> visit(TextLiteral textLiteral) {
        return Stream.of(textLiteral.value());
    }

    @Override
    public Stream<String> defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}
