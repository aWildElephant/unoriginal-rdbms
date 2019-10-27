package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Cast;
import fr.awildelephant.rdbms.ast.ColumnDefinition;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.IdentifierChain;
import fr.awildelephant.rdbms.ast.value.And;
import fr.awildelephant.rdbms.ast.value.BooleanLiteral;
import fr.awildelephant.rdbms.ast.value.DecimalLiteral;
import fr.awildelephant.rdbms.ast.value.Divide;
import fr.awildelephant.rdbms.ast.value.Equal;
import fr.awildelephant.rdbms.ast.value.Greater;
import fr.awildelephant.rdbms.ast.value.IntegerLiteral;
import fr.awildelephant.rdbms.ast.value.IntervalLiteral;
import fr.awildelephant.rdbms.ast.value.Less;
import fr.awildelephant.rdbms.ast.value.LessOrEqual;
import fr.awildelephant.rdbms.ast.value.Like;
import fr.awildelephant.rdbms.ast.value.Minus;
import fr.awildelephant.rdbms.ast.value.Multiply;
import fr.awildelephant.rdbms.ast.value.Not;
import fr.awildelephant.rdbms.ast.value.NullLiteral;
import fr.awildelephant.rdbms.ast.value.Or;
import fr.awildelephant.rdbms.ast.value.Placeholder;
import fr.awildelephant.rdbms.ast.value.Plus;
import fr.awildelephant.rdbms.ast.value.TextLiteral;
import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.Schema;

import java.time.Period;

import static fr.awildelephant.rdbms.data.value.DecimalValue.decimalValue;
import static fr.awildelephant.rdbms.data.value.FalseValue.falseValue;
import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;
import static fr.awildelephant.rdbms.data.value.IntervalValue.intervalValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.data.value.TextValue.textValue;
import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;
import static fr.awildelephant.rdbms.plan.arithmetic.AddExpression.addExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.AndExpression.andExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.CastExpression.castExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.ConstantExpression.constantExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.DivideExpression.divideExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.EqualExpression.equalExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.GreaterExpression.greaterExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.LessExpression.lessExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.LessOrEqualExpression.lessOrEqualExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.LikeExpression.likeExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.MultiplyExpression.multiplyExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.NotExpression.notExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.OrExpression.orExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.SubtractExpression.subtractExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.Variable.variable;
import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;
import static fr.awildelephant.rdbms.schema.Domain.DATE;
import static fr.awildelephant.rdbms.schema.Domain.DECIMAL;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;
import static fr.awildelephant.rdbms.schema.Domain.INTERVAL;
import static fr.awildelephant.rdbms.schema.Domain.NULL;
import static fr.awildelephant.rdbms.schema.Domain.TEXT;
import static java.lang.Integer.parseInt;

public class ASTToValueExpressionTransformer extends DefaultASTVisitor<ValueExpression> {

    private final Schema inputSchema;

    private ASTToValueExpressionTransformer(Schema inputSchema) {
        this.inputSchema = inputSchema;
    }

    static ValueExpression createValueExpression(AST tree, Schema inputSchema) {
        return new ASTToValueExpressionTransformer(inputSchema).apply(tree);
    }

    @Override
    public ValueExpression visit(And and) {
        final ValueExpression left = apply(and.left());
        final ValueExpression right = apply(and.right());

        return andExpression(left, right);
    }

    @Override
    public ValueExpression visit(BooleanLiteral booleanLiteral) {
        final DomainValue value;
        switch (booleanLiteral) {
            case TRUE:
                value = trueValue();
                break;
            case FALSE:
                value = falseValue();
                break;
            default:
                value = nullValue();
        }

        return constantExpression(value, BOOLEAN);
    }

    @Override
    public ValueExpression visit(Cast cast) {
        if (cast.targetType() != ColumnDefinition.DATE) {
            throw new UnsupportedOperationException("Unsupported cast to type " + cast.targetType());
        }

        final ValueExpression input = apply(cast.input());

        final Domain inputDomain = input.domain();

        if (inputDomain != TEXT) {
            throw new UnsupportedOperationException("Unsupported cast from " + inputDomain + " to DATE");
        }

        return castExpression(input, DATE);
    }

    @Override
    public ValueExpression visit(IdentifierChain identifierChain) {
        final String columnName = identifierChain.last();

        return variable(columnName, inputSchema.column(columnName).domain());
    }

    @Override
    public ValueExpression visit(DecimalLiteral decimalLiteral) {
        return constantExpression(decimalValue(decimalLiteral.value()), DECIMAL);
    }

    @Override
    public ValueExpression visit(Divide divide) {
        final ValueExpression left = apply(divide.left());
        final ValueExpression right = apply(divide.right());

        if (left.domain().canBeUsedAs(DECIMAL) && right.domain().canBeUsedAs(DECIMAL)) {
            return divideExpression(left, right);
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public ValueExpression visit(Equal equal) {
        final ValueExpression left = apply(equal.left());
        final ValueExpression right = apply(equal.right());

        return equalExpression(left, right);
    }

    @Override
    public ValueExpression visit(Greater greater) {
        final ValueExpression left = apply(greater.left());
        final ValueExpression right = apply(greater.right());

        return greaterExpression(left, right);
    }


    @Override
    public ValueExpression visit(IntegerLiteral integerLiteral) {
        return constantExpression(integerValue(integerLiteral.value()), INTEGER);
    }

    @Override
    public ValueExpression visit(IntervalLiteral intervalLiteral) {
        final Period period = Period.ofDays(parseInt(intervalLiteral.intervalString()));

        return constantExpression(intervalValue(period), INTERVAL);
    }

    @Override
    public ValueExpression visit(Less less) {
        final ValueExpression left = apply(less.left());
        final ValueExpression right = apply(less.right());

        return lessExpression(left, right);
    }

    @Override
    public ValueExpression visit(LessOrEqual lessOrEqual) {
        final ValueExpression left = apply(lessOrEqual.left());
        final ValueExpression right = apply(lessOrEqual.right());

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
    public ValueExpression visit(Minus minus) {
        final ValueExpression left = apply(minus.left());
        final ValueExpression right = apply(minus.right());

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
        final ValueExpression left = apply(multiply.left());
        final ValueExpression right = apply(multiply.right());

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
        return notExpression(apply(not.input()));
    }

    @Override
    public ValueExpression visit(NullLiteral nullLiteral) {
        return constantExpression(nullValue(), NULL);
    }

    @Override
    public ValueExpression visit(Or or) {
        final ValueExpression left = apply(or.left());
        final ValueExpression right = apply(or.right());

        return orExpression(left, right);
    }

    @Override
    public ValueExpression visit(Placeholder placeholder) {
        throw new UnsupportedOperationException("Placeholders are not yet supported");
    }

    @Override
    public ValueExpression visit(Plus plus) {
        final ValueExpression left = apply(plus.left());
        final ValueExpression right = apply(plus.right());

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
    public ValueExpression visit(TextLiteral textLiteral) {
        return constantExpression(textValue(textLiteral.value()), TEXT);
    }

    @Override
    public ValueExpression defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}
