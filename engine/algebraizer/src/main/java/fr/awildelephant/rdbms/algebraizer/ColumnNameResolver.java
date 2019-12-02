package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.Exists;
import fr.awildelephant.rdbms.ast.QualifiedColumnName;
import fr.awildelephant.rdbms.ast.Substring;
import fr.awildelephant.rdbms.ast.UnqualifiedColumnName;
import fr.awildelephant.rdbms.ast.value.And;
import fr.awildelephant.rdbms.ast.value.Avg;
import fr.awildelephant.rdbms.ast.value.Between;
import fr.awildelephant.rdbms.ast.value.BooleanLiteral;
import fr.awildelephant.rdbms.ast.value.CaseWhen;
import fr.awildelephant.rdbms.ast.value.Count;
import fr.awildelephant.rdbms.ast.value.CountStar;
import fr.awildelephant.rdbms.ast.value.DecimalLiteral;
import fr.awildelephant.rdbms.ast.value.Divide;
import fr.awildelephant.rdbms.ast.value.Equal;
import fr.awildelephant.rdbms.ast.value.ExtractYear;
import fr.awildelephant.rdbms.ast.value.Greater;
import fr.awildelephant.rdbms.ast.value.GreaterOrEqual;
import fr.awildelephant.rdbms.ast.value.In;
import fr.awildelephant.rdbms.ast.value.IntegerLiteral;
import fr.awildelephant.rdbms.ast.value.Less;
import fr.awildelephant.rdbms.ast.value.LessOrEqual;
import fr.awildelephant.rdbms.ast.value.Like;
import fr.awildelephant.rdbms.ast.value.Max;
import fr.awildelephant.rdbms.ast.value.Min;
import fr.awildelephant.rdbms.ast.value.Minus;
import fr.awildelephant.rdbms.ast.value.Multiply;
import fr.awildelephant.rdbms.ast.value.Not;
import fr.awildelephant.rdbms.ast.value.NotEqual;
import fr.awildelephant.rdbms.ast.value.NullLiteral;
import fr.awildelephant.rdbms.ast.value.Or;
import fr.awildelephant.rdbms.ast.value.Plus;
import fr.awildelephant.rdbms.ast.value.Sum;
import fr.awildelephant.rdbms.ast.value.TextLiteral;

import java.util.StringJoiner;

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
    public String visit(Between between) {
        return apply(between.value()) + " between " + apply(between.lowerBound()) + " and "
                + apply(between.upperBound());
    }

    @Override
    public String visit(BooleanLiteral booleanLiteral) {
        return booleanLiteral.toString().toLowerCase();
    }

    @Override
    public String visit(CaseWhen caseWhen) {
        return "case when " + apply(caseWhen.condition()) + " then " + apply(caseWhen.thenExpression()) + " else "
                + apply(caseWhen.elseExpression()) + " end";
    }

    @Override
    public String visit(Count count) {
        final StringBuilder sb = new StringBuilder("count(");
        if (count.distinct()) {
            sb.append("distinct ");
        }
        sb.append(apply(count.input()));
        sb.append(')');

        return sb.toString();
    }

    @Override
    public String visit(CountStar countStar) {
        return "count(*)";
    }

    @Override
    public String visit(Exists exists) {
        return "exists(" + apply(exists.input()) + ')';
    }

    @Override
    public String visit(ExtractYear extractYear) {
        return "extract(year from " + apply(extractYear.input()) + ')';
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
    public String visit(GreaterOrEqual greaterOrEqual) {
        final String left = apply(greaterOrEqual.left());
        final String right = apply(greaterOrEqual.right());

        return left + " >= " + right;
    }

    @Override
    public String visit(In in) {
        final StringJoiner joiner = new StringJoiner(",", apply(in.input()) + " in (", ")");
        in.values().forEach(value -> joiner.add(apply(value)));

        return joiner.toString();
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
    public String visit(Max max) {
        return "max(" + apply(max.input()) + ')';
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
    public String visit(NotEqual notEqual) {
        final String left = apply(notEqual.left());
        final String right = apply(notEqual.right());

        return left + " <> " + right;
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
    public String visit(QualifiedColumnName qualifiedColumnReference) {
        return qualifiedColumnReference.qualifier() + '.' + qualifiedColumnReference.name();
    }

    @Override
    public String visit(Substring substring) {
        return "substring(" + apply(substring.input()) + " from " + apply(substring.start()) + " for " + apply(
                substring.length()) + ")";
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
    public String visit(UnqualifiedColumnName unqualifiedColumnReference) {
        return unqualifiedColumnReference.name();
    }

    @Override
    public String defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}
