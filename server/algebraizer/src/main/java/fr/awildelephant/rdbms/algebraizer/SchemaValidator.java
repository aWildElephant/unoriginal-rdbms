package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Asterisk;
import fr.awildelephant.rdbms.ast.Cast;
import fr.awildelephant.rdbms.ast.ColumnAlias;
import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.value.CountStar;
import fr.awildelephant.rdbms.ast.value.DecimalLiteral;
import fr.awildelephant.rdbms.ast.value.Divide;
import fr.awildelephant.rdbms.ast.value.IntegerLiteral;
import fr.awildelephant.rdbms.ast.value.Minus;
import fr.awildelephant.rdbms.ast.value.Multiply;
import fr.awildelephant.rdbms.ast.value.NullLiteral;
import fr.awildelephant.rdbms.ast.value.Plus;
import fr.awildelephant.rdbms.ast.value.TextLiteral;
import fr.awildelephant.rdbms.schema.Schema;

public class SchemaValidator extends DefaultASTVisitor<Void> {

    private final Schema inputSchema;

    private SchemaValidator(Schema inputSchema) {
        this.inputSchema = inputSchema;
    }

    static SchemaValidator schemaValidator(Schema inputSchema) {
        return new SchemaValidator(inputSchema);
    }

    @Override
    public Void visit(Asterisk asterisk) {
        return null;
    }

    @Override
    public Void visit(Cast cast) {
        apply(cast.input());

        return null;
    }

    @Override
    public Void visit(ColumnAlias columnAlias) {
        apply(columnAlias.input());

        return null;
    }

    @Override
    public Void visit(ColumnName columnName) {
        final String name = columnName.name();

        if (!inputSchema.contains(name)) {
            throw new IllegalStateException("Column not found: " + name);
        }

        return null;
    }

    @Override
    public Void visit(CountStar countStar) {
        return null;
    }

    @Override
    public Void visit(DecimalLiteral decimalLiteral) {
        return null;
    }

    @Override
    public Void visit(Divide divide) {
        apply(divide.left());
        apply(divide.right());

        return null;
    }

    @Override
    public Void visit(IntegerLiteral integerLiteral) {
        return null;
    }

    @Override
    public Void visit(Minus minus) {
        apply(minus.left());
        apply(minus.right());

        return null;
    }

    @Override
    public Void visit(Multiply multiply) {
        apply(multiply.left());
        apply(multiply.right());

        return null;
    }

    @Override
    public Void visit(NullLiteral nullLiteral) {
        return null;
    }

    @Override
    public Void visit(Plus plus) {
        apply(plus.left());
        apply(plus.right());

        return null;
    }

    @Override
    public Void visit(TextLiteral textLiteral) {
        return null;
    }

    @Override
    public Void defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}
