package fr.awildelephant.rdbms.formula.creation;

import fr.awildelephant.rdbms.arithmetic.AddExpression;
import fr.awildelephant.rdbms.arithmetic.AndExpression;
import fr.awildelephant.rdbms.arithmetic.BetweenExpression;
import fr.awildelephant.rdbms.arithmetic.CaseWhenExpression;
import fr.awildelephant.rdbms.arithmetic.CastExpression;
import fr.awildelephant.rdbms.arithmetic.CoalesceExpression;
import fr.awildelephant.rdbms.arithmetic.ConstantExpression;
import fr.awildelephant.rdbms.arithmetic.DefaultValueExpressionVisitor;
import fr.awildelephant.rdbms.arithmetic.DivideExpression;
import fr.awildelephant.rdbms.arithmetic.EqualExpression;
import fr.awildelephant.rdbms.arithmetic.ExtractYearExpression;
import fr.awildelephant.rdbms.arithmetic.GreaterExpression;
import fr.awildelephant.rdbms.arithmetic.GreaterOrEqualExpression;
import fr.awildelephant.rdbms.arithmetic.InExpression;
import fr.awildelephant.rdbms.arithmetic.IsNullExpression;
import fr.awildelephant.rdbms.arithmetic.LessExpression;
import fr.awildelephant.rdbms.arithmetic.LessOrEqualExpression;
import fr.awildelephant.rdbms.arithmetic.LikeExpression;
import fr.awildelephant.rdbms.arithmetic.MultiplyExpression;
import fr.awildelephant.rdbms.arithmetic.NotEqualExpression;
import fr.awildelephant.rdbms.arithmetic.NotExpression;
import fr.awildelephant.rdbms.arithmetic.OrExpression;
import fr.awildelephant.rdbms.arithmetic.OuterQueryVariable;
import fr.awildelephant.rdbms.arithmetic.SubstringExpression;
import fr.awildelephant.rdbms.arithmetic.SubtractExpression;
import fr.awildelephant.rdbms.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.arithmetic.Variable;
import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.evaluator.operation.CoalesceOperation;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.ValuesHolder;
import fr.awildelephant.rdbms.evaluator.operation.bool.AndOperation;
import fr.awildelephant.rdbms.evaluator.operation.bool.BooleanConstant;
import fr.awildelephant.rdbms.evaluator.operation.bool.BooleanOperation;
import fr.awildelephant.rdbms.evaluator.operation.bool.BooleanVariable;
import fr.awildelephant.rdbms.evaluator.operation.bool.IsNullPredicate;
import fr.awildelephant.rdbms.evaluator.operation.bool.LikePredicate;
import fr.awildelephant.rdbms.evaluator.operation.bool.NotOperation;
import fr.awildelephant.rdbms.evaluator.operation.bool.OrOperation;
import fr.awildelephant.rdbms.evaluator.operation.bool.comparison.DomainValueUtils;
import fr.awildelephant.rdbms.evaluator.operation.cast.CastOperationFactory;
import fr.awildelephant.rdbms.evaluator.operation.date.DateConstant;
import fr.awildelephant.rdbms.evaluator.operation.date.DateIntervalAddition;
import fr.awildelephant.rdbms.evaluator.operation.date.DateIntervalSubstraction;
import fr.awildelephant.rdbms.evaluator.operation.date.DateOperation;
import fr.awildelephant.rdbms.evaluator.operation.date.DateVariable;
import fr.awildelephant.rdbms.evaluator.operation.interval.IntervalConstant;
import fr.awildelephant.rdbms.evaluator.operation.interval.IntervalOperation;
import fr.awildelephant.rdbms.evaluator.operation.interval.IntervalVariable;
import fr.awildelephant.rdbms.evaluator.operation.nil.NullConstant;
import fr.awildelephant.rdbms.evaluator.operation.numeric.DecimalAddition;
import fr.awildelephant.rdbms.evaluator.operation.numeric.DecimalConstant;
import fr.awildelephant.rdbms.evaluator.operation.numeric.DecimalDivision;
import fr.awildelephant.rdbms.evaluator.operation.numeric.DecimalMultiplication;
import fr.awildelephant.rdbms.evaluator.operation.numeric.DecimalOperation;
import fr.awildelephant.rdbms.evaluator.operation.numeric.DecimalSubtraction;
import fr.awildelephant.rdbms.evaluator.operation.numeric.DecimalVariable;
import fr.awildelephant.rdbms.evaluator.operation.numeric.ExtractYearOperation;
import fr.awildelephant.rdbms.evaluator.operation.numeric.IntegerAddition;
import fr.awildelephant.rdbms.evaluator.operation.numeric.IntegerConstant;
import fr.awildelephant.rdbms.evaluator.operation.numeric.IntegerDivision;
import fr.awildelephant.rdbms.evaluator.operation.numeric.IntegerMultiplication;
import fr.awildelephant.rdbms.evaluator.operation.numeric.IntegerOperation;
import fr.awildelephant.rdbms.evaluator.operation.numeric.IntegerSubtraction;
import fr.awildelephant.rdbms.evaluator.operation.numeric.IntegerVariable;
import fr.awildelephant.rdbms.evaluator.operation.numeric.LongConstant;
import fr.awildelephant.rdbms.evaluator.operation.numeric.LongVariable;
import fr.awildelephant.rdbms.evaluator.operation.text.SubstringOperation;
import fr.awildelephant.rdbms.evaluator.operation.text.TextConstant;
import fr.awildelephant.rdbms.evaluator.operation.text.TextOperation;
import fr.awildelephant.rdbms.evaluator.operation.text.TextVariable;
import fr.awildelephant.rdbms.formula.exception.ExtractIncompatibleTypeError;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.OrderedColumnMetadata;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.function.Supplier;

import static fr.awildelephant.rdbms.evaluator.operation.CaseWhenOperation.caseWhenOperation;
import static fr.awildelephant.rdbms.evaluator.operation.bool.comparison.ComparisonFactory.equalComparison;
import static fr.awildelephant.rdbms.evaluator.operation.bool.comparison.ComparisonFactory.lessComparison;
import static fr.awildelephant.rdbms.evaluator.operation.bool.comparison.ComparisonFactory.lessOrEqualComparison;
import static fr.awildelephant.rdbms.evaluator.operation.bool.comparison.DomainValueUtils.extractBigDecimal;
import static fr.awildelephant.rdbms.evaluator.operation.bool.comparison.DomainValueUtils.extractInteger;
import static fr.awildelephant.rdbms.evaluator.operation.bool.comparison.DomainValueUtils.extractLong;

public final class ValueExpressionToFormulaTransformer extends DefaultValueExpressionVisitor<Operation> {

    private final Schema schema;
    private final ValuesHolder valuesHolder;

    public ValueExpressionToFormulaTransformer(Schema schema) {
        this.schema = schema;
        this.valuesHolder = new ValuesHolder();
    }

    private ValuesHolder valuesHolder() {
        return valuesHolder;
    }

    public static Formula createFormula(ValueExpression expression, Schema inputSchema) {
        final ValueExpressionToFormulaTransformer transformer = new ValueExpressionToFormulaTransformer(inputSchema);

        return new Formula(transformer.apply(expression), transformer.valuesHolder());
    }

    public static Formula createFormula(ValueExpression expression, Schema firstInputSchema, Schema secondInputSchema) {
        final Schema joinedSchema = firstInputSchema.extend(secondInputSchema.columnMetadataList());

        return createFormula(expression, joinedSchema);
    }

    @Override
    public Operation visit(final AddExpression add) {
        final Operation left = apply(add.left());
        final Operation right = apply(add.right());

        if (left instanceof IntegerOperation && right instanceof IntegerOperation) {
            return new IntegerAddition((IntegerOperation) left, (IntegerOperation) right);
        }

        if (left instanceof DecimalOperation && right instanceof DecimalOperation) {
            return new DecimalAddition((DecimalOperation) left, (DecimalOperation) right);
        }

        if (left instanceof DateOperation && right instanceof IntervalOperation) {
            return new DateIntervalAddition((DateOperation) left, (IntervalOperation) right);
        }

        throw new IllegalStateException();
    }

    @Override
    public AndOperation visit(AndExpression and) {
        final BooleanOperation left = (BooleanOperation) apply(and.left());
        return new AndOperation(left, (BooleanOperation) apply(and.right()));
    }

    @Override
    public AndOperation visit(BetweenExpression between) {
        final Operation value = apply(between.value());
        final Operation lowerBound = apply(between.lowerBound());
        final Operation upperBound = apply(between.upperBound());

        final BooleanOperation left = lessOrEqualComparison(lowerBound, value);
        return new AndOperation(left, lessOrEqualComparison(value, upperBound));
    }

    @Override
    public Operation visit(CaseWhenExpression caseWhen) {
        final Operation condition = apply(caseWhen.condition());
        final Operation thenExpression = apply(caseWhen.thenExpression());
        final Operation elseExpression = apply(caseWhen.elseExpression());

        return caseWhenOperation(condition, thenExpression, elseExpression, caseWhen.domain());
    }

    @Override
    public Operation visit(CastExpression cast) {
        return CastOperationFactory.build(apply(cast.child()), cast.domain());
    }

    @Override
    public Operation visit(CoalesceExpression coalesce) {
        return new CoalesceOperation(coalesce.children().stream().map(this).toList());
    }

    @Override
    public Operation visit(ConstantExpression constantExpression) {
        final DomainValue domainValue = constantExpression.value();
        final Domain domain = constantExpression.domain();
        return switch (domain) {
            case BOOLEAN -> new BooleanConstant(DomainValueUtils.extractThreeValuedLogic(domainValue));
            case DATE -> new DateConstant(DomainValueUtils.extractLocalDate(domainValue));
            case DECIMAL -> new DecimalConstant(extractBigDecimal(domainValue));
            case INTEGER -> new IntegerConstant(extractInteger(domainValue));
            case INTERVAL -> new IntervalConstant(DomainValueUtils.extractPeriod(domainValue));
            case LONG -> new LongConstant(extractLong(domainValue));
            case NULL -> new NullConstant();
            case TEXT -> new TextConstant(DomainValueUtils.extractString(domainValue));
            default -> throw new IllegalStateException("Cannot create constant of type " + domain);
        };
    }

    @Override
    public Operation visit(final DivideExpression divide) {
        final Operation left = apply(divide.left());
        final Operation right = apply(divide.right());

        if (left instanceof IntegerOperation && right instanceof IntegerOperation) {
            return new IntegerDivision((IntegerOperation) left, (IntegerOperation) right);
        }

        if (left instanceof DecimalOperation && right instanceof DecimalOperation) {
            return new DecimalDivision((DecimalOperation) left, (DecimalOperation) right);
        }

        throw new IllegalStateException();
    }

    @Override
    public Operation visit(EqualExpression equal) {
        final Operation left = apply(equal.left());
        final Operation right = apply(equal.right());

        return equalComparison(left, right);
    }

    @Override
    public Operation visit(ExtractYearExpression extractYear) {
        final Operation input = apply(extractYear.child());

        if (!(input instanceof final DateOperation dateOperation)) {
            throw new ExtractIncompatibleTypeError("YEAR", input.domain().name());
        }

        return new ExtractYearOperation(dateOperation);
    }

    @Override
    public Operation visit(GreaterExpression greater) {
        final Operation left = apply(greater.left());
        final Operation right = apply(greater.right());

        return lessComparison(right, left);
    }

    @Override
    public Operation visit(GreaterOrEqualExpression greaterOrEqual) {
        final Operation left = apply(greaterOrEqual.left());
        final Operation right = apply(greaterOrEqual.right());

        return lessOrEqualComparison(right, left);
    }

    @Override
    public BooleanOperation visit(InExpression in) {
        final Operation input = apply(in.input());

        BooleanOperation inOperation = null;

        for (ValueExpression value : in.values()) {
            final BooleanOperation comparison = equalComparison(input, apply(value));
            if (inOperation != null) {
                inOperation = new OrOperation(inOperation, comparison);
            } else {
                inOperation = comparison;
            }
        }

        return inOperation;
    }

    @Override
    public Operation visit(IsNullExpression isNull) {
        return new IsNullPredicate(apply(isNull.child()));
    }

    @Override
    public Operation visit(LessExpression less) {
        final Operation left = apply(less.left());
        final Operation right = apply(less.right());

        return lessComparison(left, right);
    }

    @Override
    public Operation visit(LessOrEqualExpression lessOrEqual) {
        final Operation left = apply(lessOrEqual.left());
        final Operation right = apply(lessOrEqual.right());

        return lessOrEqualComparison(left, right);
    }

    @Override
    public Operation visit(LikeExpression like) {
        final TextOperation value = (TextOperation) apply(like.input());
        final TextOperation pattern = (TextOperation) apply(like.pattern());

        return new LikePredicate(value, pattern);
    }

    @Override
    public Operation visit(final MultiplyExpression multiply) {
        final Operation left = apply(multiply.left());
        final Operation right = apply(multiply.right());

        if (left instanceof IntegerOperation && right instanceof IntegerOperation) {
            return new IntegerMultiplication((IntegerOperation) left, (IntegerOperation) right);
        }

        if (left instanceof DecimalOperation && right instanceof DecimalOperation) {
            return new DecimalMultiplication((DecimalOperation) left, (DecimalOperation) right);
        }

        throw new IllegalStateException();
    }

    @Override
    public Operation visit(NotExpression not) {
        return new NotOperation((BooleanOperation) apply(not.child()));
    }

    @Override
    public Operation visit(NotEqualExpression notEqualExpression) {
        final Operation left = apply(notEqualExpression.left());
        final Operation right = apply(notEqualExpression.right());

        return new NotOperation(equalComparison(left, right));
    }

    @Override
    public OrOperation visit(OrExpression or) {
        BooleanOperation left = (BooleanOperation) apply(or.left());
        return new OrOperation(left, (BooleanOperation) apply(or.right()));
    }

    @Override
    public Operation visit(OuterQueryVariable outerQueryVariable) {
        final String error = "Unresolved outer query reference to " + outerQueryVariable.reference().fullName();

        throw new IllegalStateException(error);
    }

    @Override
    public Operation visit(SubstringExpression substring) {
        final TextOperation input = (TextOperation) apply(substring.input());
        final IntegerOperation start = (IntegerOperation) apply(substring.start());
        final IntegerOperation length = (IntegerOperation) apply(substring.length());

        return new SubstringOperation(input, start, length);
    }

    @Override
    public Operation visit(SubtractExpression subtract) {
        final Operation left = apply(subtract.left());
        final Operation right = apply(subtract.right());

        if (left instanceof IntegerOperation && right instanceof IntegerOperation) {
            return new IntegerSubtraction((IntegerOperation) left, (IntegerOperation) right);
        }

        if (left instanceof DecimalOperation && right instanceof DecimalOperation) {
            return new DecimalSubtraction((DecimalOperation) left, (DecimalOperation) right);
        }

        if (left instanceof DateOperation && right instanceof IntervalOperation) {
            return new DateIntervalSubstraction((DateOperation) left, (IntervalOperation) right);
        }

        throw new IllegalStateException();
    }

    @Override
    public Operation visit(Variable variable) {
        final OrderedColumnMetadata column = schema.column(variable.reference());

        final Domain domain = column.metadata().domain();
        final Supplier<DomainValue> domainValueSupplier = valuesHolder.createSupplier(column.index());
        return switch (domain) {
            case BOOLEAN ->
                    new BooleanVariable(() -> DomainValueUtils.extractThreeValuedLogic(domainValueSupplier.get()));
            case DATE -> new DateVariable(() -> DomainValueUtils.extractLocalDate(domainValueSupplier.get()));
            case DECIMAL -> new DecimalVariable(() -> extractBigDecimal(domainValueSupplier.get()));
            case INTEGER -> new IntegerVariable(() -> extractInteger(domainValueSupplier.get()));
            case INTERVAL -> new IntervalVariable(() -> DomainValueUtils.extractPeriod(domainValueSupplier.get()));
            case LONG -> new LongVariable(() -> extractLong(domainValueSupplier.get()));
            case NULL -> new NullConstant();
            case TEXT -> new TextVariable(() -> DomainValueUtils.extractString(domainValueSupplier.get()));
            default -> throw new IllegalStateException("Cannot create variable of type " + domain);
        };
    }

    @Override
    protected Operation defaultVisit(ValueExpression expression) {
        throw new IllegalStateException();
    }
}
