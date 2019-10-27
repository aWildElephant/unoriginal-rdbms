package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.QualifiedColumnReference;
import fr.awildelephant.rdbms.ast.UnqualifiedColumnReference;
import fr.awildelephant.rdbms.ast.value.And;
import fr.awildelephant.rdbms.ast.value.Avg;
import fr.awildelephant.rdbms.ast.value.BooleanLiteral;
import fr.awildelephant.rdbms.ast.value.CountStar;
import fr.awildelephant.rdbms.ast.value.DecimalLiteral;
import fr.awildelephant.rdbms.ast.value.Divide;
import fr.awildelephant.rdbms.ast.value.Equal;
import fr.awildelephant.rdbms.ast.value.Greater;
import fr.awildelephant.rdbms.ast.value.IntegerLiteral;
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

public final class ColumnNameResolver extends DefaultASTVisitor<String> {

    @Override
    public String visit(And and) {
        return apply(and.left()) + " and " + apply(and.right());
    }

    @Override
    public String visit(Avg avg) {
        return "avg(" + apply(avg.input()) + ")";
    }

    @Override
    public String visit(BooleanLiteral booleanLiteral) {
        return booleanLiteral.toString().toLowerCase();
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
    public String visit(Equal equal) {
        final String left = apply(equal.left());
        final String right = apply(equal.right());

        return left + " = " + right;
    }

    @Override
    public String visit(Greater greater) {
        final String left = apply(greater.left());
        final String right = apply(greater.right());

        return left + " > " + right;
    }

    @Override
    public String visit(Less less) {
        final String left = apply(less.left());
        final String right = apply(less.right());

        return left + " < " + right;
    }

    @Override
    public String visit(LessOrEqual lessOrEqual) {
        final String left = apply(lessOrEqual.left());
        final String right = apply(lessOrEqual.right());

        return left + " <= " + right;
    }

    @Override
    public String visit(Like like) {
        final String input = apply(like.input());
        final String pattern = apply(like.pattern());

        return input + " LIKE " + pattern;
    }

    @Override
    public String visit(Min min) {
        final String input = apply(min.input());

        return "min(" + input + ")";
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
    public String visit(Not not) {
        return "not " + apply(not.input());
    }

    @Override
    public String visit(NullLiteral nullLiteral) {
        return "null";
    }

    @Override
    public String visit(Or or) {
        return apply(or.left()) + " or " + apply(or.right());
    }

    @Override
    public String visit(Plus plus) {
        final String left = apply(plus.left());
        final String right = apply(plus.right());

        return left + " + " + right;
    }

    @Override
    public String visit(QualifiedColumnReference qualifiedColumnReference) {
        return qualifiedColumnReference.qualifier() + '.' + qualifiedColumnReference.name();
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
    public String visit(UnqualifiedColumnReference unqualifiedColumnReference) {
        return unqualifiedColumnReference.name();
    }

    @Override
    public String defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}
