package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.value.BooleanLiteral;
import fr.awildelephant.rdbms.ast.value.CountStar;
import fr.awildelephant.rdbms.ast.value.DecimalLiteral;
import fr.awildelephant.rdbms.ast.value.Divide;
import fr.awildelephant.rdbms.ast.value.IntegerLiteral;
import fr.awildelephant.rdbms.ast.value.Minus;
import fr.awildelephant.rdbms.ast.value.Multiply;
import fr.awildelephant.rdbms.ast.value.NullLiteral;
import fr.awildelephant.rdbms.ast.value.Plus;
import fr.awildelephant.rdbms.ast.value.Sum;
import fr.awildelephant.rdbms.ast.value.TextLiteral;

public final class ColumnNameResolver extends DefaultASTVisitor<String> {

    @Override
    public String visit(BooleanLiteral booleanLiteral) {
        return booleanLiteral.toString().toLowerCase();
    }

    @Override
    public String visit(ColumnName columnName) {
        return columnName.name();
    }

    @Override
    public String visit(CountStar countStar) {
        return "count(*)";
    }

    @Override
    public String visit(IntegerLiteral integerLiteral) {
        return String.valueOf(integerLiteral.value());
    }

    @Override
    public String visit(DecimalLiteral decimalLiteral) {
        return decimalLiteral.value().toString();
    }

    @Override
    public String visit(Divide divide) {
        final String left = apply(divide.left());
        final String right = apply(divide.right());

        return left + " / " + right;
    }

    @Override
    public String visit(Minus minus) {
        final String left = apply(minus.left());
        final String right = apply(minus.right());

        return left + " - " + right;
    }

    @Override
    public String visit(Multiply multiply) {
        final String left = apply(multiply.left());
        final String right = apply(multiply.right());

        return left + " * " + right;
    }

    @Override
    public String visit(NullLiteral nullLiteral) {
        return "null";
    }

    @Override
    public String visit(Plus plus) {
        final String left = apply(plus.left());
        final String right = apply(plus.right());

        return left + " + " + right;
    }

    @Override
    public String visit(Sum sum) {
        return "sum(" + apply(sum.input()) + ")";
    }

    @Override
    public String visit(TextLiteral textLiteral) {
        return textLiteral.value();
    }

    @Override
    public String defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}
