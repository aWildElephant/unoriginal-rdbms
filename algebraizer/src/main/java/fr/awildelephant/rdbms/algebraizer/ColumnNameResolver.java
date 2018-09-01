package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Asterisk;
import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.value.DecimalLiteral;
import fr.awildelephant.rdbms.ast.value.IntegerLiteral;
import fr.awildelephant.rdbms.ast.value.Multiply;
import fr.awildelephant.rdbms.ast.value.NullLiteral;
import fr.awildelephant.rdbms.ast.value.Plus;
import fr.awildelephant.rdbms.ast.value.TextLiteral;
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
    public Stream<String> visit(Multiply multiply) {
        final String left = apply(multiply.left()).findFirst().get();
        final String right = apply(multiply.right()).findFirst().get();

        return Stream.of(left + " * " + right);
    }

    @Override
    public Stream<String> visit(NullLiteral nullLiteral) {
        return Stream.of("null");
    }

    // TODO: probably the resolver shouldn't return a Stream
    @Override
    public Stream<String> visit(Plus plus) {
        final String left = apply(plus.left()).findFirst().get();
        final String right = apply(plus.right()).findFirst().get();

        return Stream.of(left + " + " + right);
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
