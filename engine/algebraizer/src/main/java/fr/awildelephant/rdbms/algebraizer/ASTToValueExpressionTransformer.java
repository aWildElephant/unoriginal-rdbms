package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Cast;
import fr.awildelephant.rdbms.ast.InValueList;
import fr.awildelephant.rdbms.ast.QualifiedColumnName;
import fr.awildelephant.rdbms.ast.Substring;
import fr.awildelephant.rdbms.ast.UnqualifiedColumnName;
import fr.awildelephant.rdbms.ast.value.And;
import fr.awildelephant.rdbms.ast.value.Between;
import fr.awildelephant.rdbms.ast.value.BooleanLiteral;
import fr.awildelephant.rdbms.ast.value.CaseWhen;
import fr.awildelephant.rdbms.ast.value.DecimalLiteral;
import fr.awildelephant.rdbms.ast.value.Divide;
import fr.awildelephant.rdbms.ast.value.Equal;
import fr.awildelephant.rdbms.ast.value.ExtractYear;
import fr.awildelephant.rdbms.ast.value.Greater;
import fr.awildelephant.rdbms.ast.value.GreaterOrEqual;
import fr.awildelephant.rdbms.ast.value.In;
import fr.awildelephant.rdbms.ast.value.IntegerLiteral;
import fr.awildelephant.rdbms.ast.value.IntervalLiteral;
import fr.awildelephant.rdbms.ast.value.IsNull;
import fr.awildelephant.rdbms.ast.value.Less;
import fr.awildelephant.rdbms.ast.value.LessOrEqual;
import fr.awildelephant.rdbms.ast.value.Like;
import fr.awildelephant.rdbms.ast.value.LongLiteral;
import fr.awildelephant.rdbms.ast.value.Minus;
import fr.awildelephant.rdbms.ast.value.Multiply;
import fr.awildelephant.rdbms.ast.value.Not;
import fr.awildelephant.rdbms.ast.value.NotEqual;
import fr.awildelephant.rdbms.ast.value.NullLiteral;
import fr.awildelephant.rdbms.ast.value.Or;
import fr.awildelephant.rdbms.ast.value.Placeholder;
import fr.awildelephant.rdbms.ast.value.Plus;
import fr.awildelephant.rdbms.ast.value.TextLiteral;
import fr.awildelephant.rdbms.ast.visitor.DefaultASTVisitor;
import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.ColumnNotFoundError;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.QualifiedColumnReference;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.arithmetic.AddExpression.addExpression;
import static fr.awildelephant.rdbms.arithmetic.AndExpression.andExpression;
import static fr.awildelephant.rdbms.arithmetic.BetweenExpression.betweenExpression;
import static fr.awildelephant.rdbms.arithmetic.CaseWhenExpression.caseWhenExpression;
import static fr.awildelephant.rdbms.arithmetic.CastExpression.castExpression;
import static fr.awildelephant.rdbms.arithmetic.ConstantExpression.constantExpression;
import static fr.awildelephant.rdbms.arithmetic.DivideExpression.divideExpression;
import static fr.awildelephant.rdbms.arithmetic.EqualExpression.equalExpression;
import static fr.awildelephant.rdbms.arithmetic.ExtractYearExpression.extractYearExpression;
import static fr.awildelephant.rdbms.arithmetic.GreaterExpression.greaterExpression;
import static fr.awildelephant.rdbms.arithmetic.GreaterOrEqualExpression.greaterOrEqualExpression;
import static fr.awildelephant.rdbms.arithmetic.InExpression.inExpression;
import static fr.awildelephant.rdbms.arithmetic.IsNullExpression.isNullExpression;
import static fr.awildelephant.rdbms.arithmetic.LessExpression.lessExpression;
import static fr.awildelephant.rdbms.arithmetic.LessOrEqualExpression.lessOrEqualExpression;
import static fr.awildelephant.rdbms.arithmetic.LikeExpression.likeExpression;
import static fr.awildelephant.rdbms.arithmetic.MultiplyExpression.multiplyExpression;
import static fr.awildelephant.rdbms.arithmetic.NotEqualExpression.notEqualExpression;
import static fr.awildelephant.rdbms.arithmetic.NotExpression.notExpression;
import static fr.awildelephant.rdbms.arithmetic.OrExpression.orExpression;
import static fr.awildelephant.rdbms.arithmetic.OuterQueryVariable.outerQueryVariable;
import static fr.awildelephant.rdbms.arithmetic.SubstringExpression.substringExpression;
import static fr.awildelephant.rdbms.arithmetic.SubtractExpression.subtractExpression;
import static fr.awildelephant.rdbms.arithmetic.Variable.variable;
import static fr.awildelephant.rdbms.data.value.DecimalValue.decimalValue;
import static fr.awildelephant.rdbms.data.value.FalseValue.falseValue;
import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;
import static fr.awildelephant.rdbms.data.value.IntervalValue.intervalValue;
import static fr.awildelephant.rdbms.data.value.LongValue.longValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.data.value.TextValue.textValue;
import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;
import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;
import static fr.awildelephant.rdbms.schema.Domain.DATE;
import static fr.awildelephant.rdbms.schema.Domain.DECIMAL;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;
import static fr.awildelephant.rdbms.schema.Domain.INTERVAL;
import static fr.awildelephant.rdbms.schema.Domain.LONG;
import static fr.awildelephant.rdbms.schema.Domain.NULL;
import static fr.awildelephant.rdbms.schema.Domain.TEXT;
import static java.lang.Integer.parseInt;

public class ASTToValueExpressionTransformer extends DefaultASTVisitor<ValueExpression> {

    private final Schema inputSchema;
    private final Schema outerQuerySchema;

    private ASTToValueExpressionTransformer(Schema inputSchema, Schema outerQuerySchema) {
        this.inputSchema = inputSchema;
        this.outerQuerySchema = outerQuerySchema;
    }

    public static ValueExpression createValueExpression(AST tree, Schema inputSchema, Schema outerQuerySchema) {
        return new ASTToValueExpressionTransformer(inputSchema, outerQuerySchema).apply(tree);
    }

    @Override
    public ValueExpression visit(And and) {
        final ValueExpression left = apply(and.leftChild());
        final ValueExpression right = apply(and.rightChild());

        return andExpression(left, right);
    }

    @Override
    public ValueExpression visit(Between between) {
        final ValueExpression value = apply(between.value());
        final ValueExpression lowerBound = apply(between.lowerBound());
        final ValueExpression upperBound = apply(between.upperBound());

        return betweenExpression(value, lowerBound, upperBound);
    }

    @Override
    public ValueExpression visit(BooleanLiteral booleanLiteral) {
        final DomainValue value = switch (booleanLiteral.value()) {
            case TRUE -> trueValue();
            case FALSE -> falseValue();
            case UNKNOWN -> nullValue();
        };

        return constantExpression(value, BOOLEAN);
    }

    @Override
    public ValueExpression visit(CaseWhen caseWhen) {
        final ValueExpression condition = apply(caseWhen.condition());

        if (condition.domain() != BOOLEAN) {
            throw new IllegalArgumentException("The condition of a CASE WHEN expression must have BOOLEAN type");
        }

        final ValueExpression thenExpression = apply(caseWhen.thenExpression());
        final ValueExpression elseExpression = apply(caseWhen.elseExpression());

        final Domain thenDomain = thenExpression.domain();
        final Domain elseDomain = elseExpression.domain();

        final Domain outputDomain;
        if (elseDomain.canBeUsedAs(thenDomain)) {
            outputDomain = thenDomain;
        } else if (thenDomain.canBeUsedAs(elseDomain)) {
            outputDomain = elseDomain;
        } else {
            throw new IllegalArgumentException("Incompatible return type " + thenDomain + " for THEN branch and "
                    + elseDomain + " for ELSE branch of CASE WHEN");
        }

        return caseWhenExpression(condition, thenExpression, elseExpression, outputDomain);
    }

    @Override
    public ValueExpression visit(Cast cast) {
        final ValueExpression input = apply(cast.child());

        return castExpression(input, ColumnUtils.domainOf(cast.targetType()));
    }

    @Override
    public ValueExpression visit(DecimalLiteral decimalLiteral) {
        return constantExpression(decimalValue(decimalLiteral.value()), DECIMAL);
    }

    @Override
    public ValueExpression visit(Divide divide) {
        final ValueExpression left = apply(divide.leftChild());
        final ValueExpression right = apply(divide.rightChild());

        if (left.domain().canBeUsedAs(DECIMAL) && right.domain().canBeUsedAs(DECIMAL)) {
            return divideExpression(left, right);
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public ValueExpression visit(Equal equal) {
        final ValueExpression left = apply(equal.leftChild());
        final ValueExpression right = apply(equal.rightChild());

        return equalExpression(left, right);
    }

    @Override
    public ValueExpression visit(ExtractYear extractYear) {
        final ValueExpression input = apply(extractYear.child());

        return extractYearExpression(input);
    }

    @Override
    public ValueExpression visit(Greater greater) {
        final ValueExpression left = apply(greater.leftChild());
        final ValueExpression right = apply(greater.rightChild());

        return greaterExpression(left, right);
    }

    @Override
    public ValueExpression visit(GreaterOrEqual greaterOrEqual) {
        final ValueExpression left = apply(greaterOrEqual.leftChild());
        final ValueExpression right = apply(greaterOrEqual.rightChild());

        return greaterOrEqualExpression(left, right);
    }

    @Override
    public ValueExpression visit(In in) {
        final ValueExpression input = apply(in.input());

        final List<ValueExpression> values = new ArrayList<>();
        for (AST value : ((InValueList) in.value()).values()) {
            values.add(apply(value));
        }

        return inExpression(input, values);
    }

    @Override
    public ValueExpression visit(IntegerLiteral integerLiteral) {
        return constantExpression(integerValue(integerLiteral.value()), INTEGER);
    }

    @Override
    public ValueExpression visit(IntervalLiteral intervalLiteral) {
        final Period period = switch (intervalLiteral.granularity()) {
            case DAY_GRANULARITY -> Period.ofDays(parseInt(intervalLiteral.intervalString()));
            case MONTH_GRANULARITY -> Period.ofMonths(parseInt(intervalLiteral.intervalString()));
            default -> Period.ofYears(parseInt(intervalLiteral.intervalString()));
        };

        return constantExpression(intervalValue(period), INTERVAL);
    }

    @Override
    public ValueExpression visit(IsNull isNull) {
        return isNullExpression(apply(isNull.input()));
    }

    @Override
    public ValueExpression visit(Less less) {
        final ValueExpression left = apply(less.leftChild());
        final ValueExpression right = apply(less.rightChild());

        return lessExpression(left, right);
    }

    @Override
    public ValueExpression visit(LessOrEqual lessOrEqual) {
        final ValueExpression left = apply(lessOrEqual.leftChild());
        final ValueExpression right = apply(lessOrEqual.rightChild());

        return lessOrEqualExpression(left, right);
    }

    @Override
    public ValueExpression visit(Like like) {
        final ValueExpression input = apply(like.input());

        if (!input.domain().canBeUsedAs(TEXT)) {
            throw new UnsupportedOperationException();
        }

        final ValueExpression pattern = apply(like.pattern());

        if (!input.domain().canBeUsedAs(TEXT)) {
            throw new UnsupportedOperationException();
        }

        return likeExpression(input, pattern);
    }

    @Override
    public ValueExpression visit(LongLiteral longLiteral) {
        return constantExpression(longValue(longLiteral.value()), LONG);
    }

    @Override
    public ValueExpression visit(Minus minus) {
        final ValueExpression left = apply(minus.leftChild());
        final ValueExpression right = apply(minus.rightChild());

        if (left.domain().canBeUsedAs(INTEGER) && right.domain().canBeUsedAs(INTEGER)) {
            return subtractExpression(left, right, INTEGER);
        }

        if (left.domain().canBeUsedAs(DECIMAL) && right.domain().canBeUsedAs(DECIMAL)) {
            return subtractExpression(left, right, DECIMAL);
        }

        if (left.domain().canBeUsedAs(DATE) && right.domain().canBeUsedAs(INTERVAL)) {
            return subtractExpression(left, right, DATE);
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public ValueExpression visit(Multiply multiply) {
        final ValueExpression left = apply(multiply.leftChild());
        final ValueExpression right = apply(multiply.rightChild());

        if (left.domain().canBeUsedAs(INTEGER) && right.domain().canBeUsedAs(INTEGER)) {
            return multiplyExpression(left, right, INTEGER);
        }

        if (left.domain().canBeUsedAs(DECIMAL) && right.domain().canBeUsedAs(DECIMAL)) {
            return multiplyExpression(left, right, DECIMAL);
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public ValueExpression visit(Not not) {
        return notExpression(apply(not.child()));
    }

    @Override
    public ValueExpression visit(NotEqual notEqual) {
        return notEqualExpression(apply(notEqual.leftChild()), apply(notEqual.rightChild()));
    }

    @Override
    public ValueExpression visit(NullLiteral nullLiteral) {
        return constantExpression(nullValue(), NULL);
    }

    @Override
    public ValueExpression visit(Or or) {
        final ValueExpression left = apply(or.leftChild());
        final ValueExpression right = apply(or.rightChild());

        return orExpression(left, right);
    }

    @Override
    public ValueExpression visit(Placeholder placeholder) {
        throw new UnsupportedOperationException("Placeholders are not yet supported");
    }

    @Override
    public ValueExpression visit(Plus plus) {
        final ValueExpression left = apply(plus.leftChild());
        final ValueExpression right = apply(plus.rightChild());

        if (left.domain().canBeUsedAs(INTEGER) && right.domain().canBeUsedAs(INTEGER)) {
            return addExpression(left, right, INTEGER);
        }

        if (left.domain().canBeUsedAs(DECIMAL) && right.domain().canBeUsedAs(DECIMAL)) {
            return addExpression(left, right, DECIMAL);
        }


        if (left.domain().canBeUsedAs(DATE) && right.domain().canBeUsedAs(INTERVAL)) {
            return addExpression(left, right, DATE);
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public ValueExpression visit(QualifiedColumnName qualifiedColumnReference) {
        final QualifiedColumnReference reference = new QualifiedColumnReference(qualifiedColumnReference.qualifier(),
                qualifiedColumnReference.name());

        return createVariable(reference);
    }

    @Override
    public ValueExpression visit(Substring substring) {
        final ValueExpression input = apply(substring.input());
        final ValueExpression start = apply(substring.start());
        final ValueExpression length = apply(substring.length());

        return substringExpression(input, start, length);
    }

    @Override
    public ValueExpression visit(TextLiteral textLiteral) {
        return constantExpression(textValue(textLiteral.value()), TEXT);
    }

    @Override
    public ValueExpression visit(UnqualifiedColumnName unqualifiedColumnReference) {
        final UnqualifiedColumnReference reference = new UnqualifiedColumnReference(unqualifiedColumnReference.name());

        return createVariable(reference);
    }

    private ValueExpression createVariable(ColumnReference reference) {
        try {
            final ColumnMetadata column = inputSchema.column(reference).metadata();
            return variable(column.name(), column.domain());
        } catch (ColumnNotFoundError unused) {
            final ColumnMetadata outerColumn = outerQuerySchema.column(reference).metadata();
            return outerQueryVariable(outerColumn.name(), outerColumn.domain());
        }
    }

    @Override
    public ValueExpression defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}
