package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Asterisk;
import fr.awildelephant.rdbms.ast.Cast;
import fr.awildelephant.rdbms.ast.ColumnAlias;
import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.value.And;
import fr.awildelephant.rdbms.ast.value.Avg;
import fr.awildelephant.rdbms.ast.value.BooleanLiteral;
import fr.awildelephant.rdbms.ast.value.CountStar;
import fr.awildelephant.rdbms.ast.value.DecimalLiteral;
import fr.awildelephant.rdbms.ast.value.Divide;
import fr.awildelephant.rdbms.ast.value.Equal;
import fr.awildelephant.rdbms.ast.value.Greater;
import fr.awildelephant.rdbms.ast.value.IntegerLiteral;
import fr.awildelephant.rdbms.ast.value.IntervalLiteral;
import fr.awildelephant.rdbms.ast.value.Less;
import fr.awildelephant.rdbms.ast.value.LessOrEqual;
import fr.awildelephant.rdbms.ast.value.Like;
import fr.awildelephant.rdbms.ast.value.Min;
import fr.awildelephant.rdbms.ast.value.Minus;
import fr.awildelephant.rdbms.ast.value.Multiply;
import fr.awildelephant.rdbms.ast.value.Not;
import fr.awildelephant.rdbms.ast.value.NullLiteral;
import fr.awildelephant.rdbms.ast.value.Or;
import fr.awildelephant.rdbms.ast.value.Plus;
import fr.awildelephant.rdbms.ast.value.Sum;
import fr.awildelephant.rdbms.ast.value.TextLiteral;
import fr.awildelephant.rdbms.schema.Schema;

// TODO: having BinaryAST/UnaryAST would allow us to reduce the size of this class
public class SchemaValidator extends DefaultASTVisitor<Void> {

    private final Schema inputSchema;

    private SchemaValidator(Schema inputSchema) {
        this.inputSchema = inputSchema;
    }

    static SchemaValidator schemaValidator(Schema inputSchema) {
        return new SchemaValidator(inputSchema);
    }

    @Override
    public Void visit(And and) {
        apply(and.left());
        apply(and.right());

        return null;
    }

    @Override
    public Void visit(Asterisk asterisk) {
        return null;
    }

    @Override
    public Void visit(Avg avg) {
        apply(avg.input());

        return null;
    }

    @Override
    public Void visit(BooleanLiteral booleanLiteral) {
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
    public Void visit(Equal equal) {
        apply(equal.left());
        apply(equal.right());

        return null;
    }

    @Override
    public Void visit(Greater greater) {
        apply(greater.left());
        apply(greater.right());

        return null;
    }

    @Override
    public Void visit(IntegerLiteral integerLiteral) {
        return null;
    }

    @Override
    public Void visit(IntervalLiteral intervalLiteral) {
        return null;
    }

    @Override
    public Void visit(Less less) {
        apply(less.left());
        apply(less.right());

        return null;
    }

    @Override
    public Void visit(LessOrEqual lessOrEqual) {
        apply(lessOrEqual.left());
        apply(lessOrEqual.right());

        return null;
    }

    @Override
    public Void visit(Like like) {
        apply(like.input());
        apply(like.pattern());

        return null;
    }

    @Override
    public Void visit(Min min) {
        apply(min.input());

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
    public Void visit(Not not) {
        apply(not.input());

        return null;
    }

    @Override
    public Void visit(NullLiteral nullLiteral) {
        return null;
    }

    @Override
    public Void visit(Or or) {
        apply(or.left());
        apply(or.right());

        return null;
    }

    @Override
    public Void visit(Plus plus) {
        apply(plus.left());
        apply(plus.right());

        return null;
    }

    @Override
    public Void visit(Sum sum) {
        apply(sum.input());

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
