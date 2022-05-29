package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Exists;
import fr.awildelephant.rdbms.ast.InValueList;
import fr.awildelephant.rdbms.ast.QualifiedColumnName;
import fr.awildelephant.rdbms.ast.Substring;
import fr.awildelephant.rdbms.ast.UnqualifiedColumnName;
import fr.awildelephant.rdbms.ast.value.And;
import fr.awildelephant.rdbms.ast.value.Any;
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
import fr.awildelephant.rdbms.ast.value.IsNull;
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
import fr.awildelephant.rdbms.ast.visitor.DefaultASTVisitor;

import java.util.List;

public class ColumnNameResolverHelper extends DefaultASTVisitor<Void> {

    private final StringBuilder stringBuilder;

    public ColumnNameResolverHelper(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    @Override
    public Void visit(And and) {
        return visitBinaryOperation(and.leftChild(), " and ", and.rightChild());
    }

    @Override
    public Void visit(Any any) {
        return visitUnaryFunction("any", any.child());
    }

    @Override
    public Void visit(Avg avg) {
        return visitUnaryFunction("avg", avg.child());
    }

    @Override
    public Void visit(Between between) {
        apply(between.value());
        stringBuilder.append(" between ");
        apply(between.lowerBound());
        stringBuilder.append(" and ");
        apply(between.upperBound());

        return null;
    }

    @Override
    public Void visit(BooleanLiteral booleanLiteral) {
        stringBuilder.append(booleanLiteral.value().toString().toLowerCase());

        return null;
    }

    @Override
    public Void visit(CaseWhen caseWhen) {
        stringBuilder.append("case when ");
        apply(caseWhen.condition());
        stringBuilder.append(" then ");
        apply(caseWhen.thenExpression());
        stringBuilder.append(" else ");
        apply(caseWhen.elseExpression());
        stringBuilder.append(" end");

        return null;
    }

    @Override
    public Void visit(Count count) {
        stringBuilder.append("count(");
        if (count.distinct()) {
            stringBuilder.append("distinct ");
        }
        apply(count.child());
        stringBuilder.append(')');

        return null;
    }

    @Override
    public Void visit(CountStar countStar) {
        stringBuilder.append("count(*)");

        return null;
    }

    @Override
    public Void visit(Exists exists) {
        return visitUnaryFunction("exists", exists.child());
    }

    @Override
    public Void visit(ExtractYear extractYear) {
        stringBuilder.append("extract(year from ");
        apply(extractYear.child());
        stringBuilder.append(')');

        return null;
    }

    @Override
    public Void visit(IntegerLiteral integerLiteral) {
        stringBuilder.append(integerLiteral.value());

        return null;
    }

    @Override
    public Void visit(DecimalLiteral decimalLiteral) {
        stringBuilder.append(decimalLiteral.value().toString());

        return null;
    }

    @Override
    public Void visit(Divide divide) {
        return visitBinaryOperation(divide.leftChild(), " / ", divide.rightChild());
    }

    @Override
    public Void visit(Equal equal) {
        return visitBinaryOperation(equal.leftChild(), " = ", equal.rightChild());
    }

    @Override
    public Void visit(Greater greater) {
        return visitBinaryOperation(greater.leftChild(), " > ", greater.rightChild());
    }

    @Override
    public Void visit(GreaterOrEqual greaterOrEqual) {
        return visitBinaryOperation(greaterOrEqual.leftChild(), " >= ", greaterOrEqual.rightChild());
    }

    @Override
    public Void visit(In in) {
        apply(in.input());
        stringBuilder.append(" in (");
        apply(in.value());
        stringBuilder.append(')');

        return null;
    }

    @Override
    public Void visit(InValueList inValueList) {

        final List<AST> values = inValueList.values();
        final int numberOfValues = values.size();
        for (int i = 0; i < numberOfValues; i++) {
            apply(values.get(i));
            if (i < numberOfValues - 1) {
                stringBuilder.append(", ");
            }
        }

        return null;
    }

    @Override
    public Void visit(IsNull isNull) {
        apply(isNull.input());
        stringBuilder.append(" is null");

        return null;
    }

    @Override
    public Void visit(Less less) {
        return visitBinaryOperation(less.leftChild(), " < ", less.rightChild());
    }

    @Override
    public Void visit(LessOrEqual lessOrEqual) {
        return visitBinaryOperation(lessOrEqual.leftChild(), " <= ", lessOrEqual.rightChild());
    }

    @Override
    public Void visit(Like like) {
        return visitBinaryOperation(like.input(), " LIKE ", like.pattern());
    }

    @Override
    public Void visit(Max max) {
        return visitUnaryFunction("max", max.child());
    }

    @Override
    public Void visit(Min min) {
        return visitUnaryFunction("min", min.child());
    }

    @Override
    public Void visit(Minus minus) {
        return visitBinaryOperation(minus.leftChild(), " - ", minus.rightChild());
    }

    @Override
    public Void visit(Multiply multiply) {
        return visitBinaryOperation(multiply.leftChild(), " * ", multiply.rightChild());
    }

    @Override
    public Void visit(Not not) {
        stringBuilder.append("not ");
        apply(not.child());

        return null;
    }

    @Override
    public Void visit(NotEqual notEqual) {
        return visitBinaryOperation(notEqual.leftChild(), " <> ", notEqual.rightChild());
    }

    @Override
    public Void visit(NullLiteral nullLiteral) {
        stringBuilder.append("null");

        return null;
    }

    @Override
    public Void visit(Or or) {
        return visitBinaryOperation(or.leftChild(), " or ", or.rightChild());
    }

    @Override
    public Void visit(Plus plus) {
        return visitBinaryOperation(plus.leftChild(), " + ", plus.rightChild());
    }

    @Override
    public Void visit(QualifiedColumnName qualifiedColumnReference) {
        stringBuilder.append(qualifiedColumnReference.qualifier());
        stringBuilder.append('.');
        stringBuilder.append(qualifiedColumnReference.name());

        return null;
    }

    @Override
    public Void visit(Substring substring) {
        stringBuilder.append("substring(");
        apply(substring.input());
        stringBuilder.append(" from ");
        apply(substring.start());
        stringBuilder.append(" for ");
        apply(substring.length());
        stringBuilder.append(")");

        return null;
    }

    @Override
    public Void visit(Sum sum) {
        return visitUnaryFunction("sum", sum.child());
    }

    @Override
    public Void visit(TextLiteral textLiteral) {
        stringBuilder.append('\'');
        stringBuilder.append(textLiteral.value());
        stringBuilder.append('\'');

        return null;
    }

    @Override
    public Void visit(UnqualifiedColumnName unqualifiedColumnReference) {
        stringBuilder.append(unqualifiedColumnReference.name());

        return null;
    }

    private Void visitBinaryOperation(AST left, String operationString, AST right) {
        apply(left);
        stringBuilder.append(operationString);
        apply(right);

        return null;
    }

    private Void visitUnaryFunction(String name, AST input) {
        stringBuilder.append(name);
        stringBuilder.append('(');
        apply(input);
        stringBuilder.append(')');
        return null;
    }

    @Override
    public Void defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}
