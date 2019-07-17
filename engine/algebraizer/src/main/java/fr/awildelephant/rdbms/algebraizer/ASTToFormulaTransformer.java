package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Cast;
import fr.awildelephant.rdbms.ast.ColumnDefinition;
import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
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
import fr.awildelephant.rdbms.ast.value.Minus;
import fr.awildelephant.rdbms.ast.value.Multiply;
import fr.awildelephant.rdbms.ast.value.Not;
import fr.awildelephant.rdbms.ast.value.NullLiteral;
import fr.awildelephant.rdbms.ast.value.Or;
import fr.awildelephant.rdbms.ast.value.Placeholder;
import fr.awildelephant.rdbms.ast.value.Plus;
import fr.awildelephant.rdbms.ast.value.TextLiteral;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.Reference;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.Schema;

import java.time.Period;
import java.util.HashMap;
import java.util.Map;

import static fr.awildelephant.rdbms.data.value.DecimalValue.decimalValue;
import static fr.awildelephant.rdbms.data.value.FalseValue.falseValue;
import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;
import static fr.awildelephant.rdbms.data.value.IntervalValue.intervalValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.data.value.TextValue.textValue;
import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;
import static fr.awildelephant.rdbms.evaluator.operation.AndOperation.andOperation;
import static fr.awildelephant.rdbms.evaluator.operation.Constant.constant;
import static fr.awildelephant.rdbms.evaluator.operation.DateCastOperation.dateCastOperation;
import static fr.awildelephant.rdbms.evaluator.operation.DateIntervalAddition.dateIntervalAddition;
import static fr.awildelephant.rdbms.evaluator.operation.DateIntervalSubstraction.dateIntervalSubstraction;
import static fr.awildelephant.rdbms.evaluator.operation.DecimalAddition.decimalAddition;
import static fr.awildelephant.rdbms.evaluator.operation.DecimalDivision.decimalDivision;
import static fr.awildelephant.rdbms.evaluator.operation.DecimalMultiplication.decimalMultiplication;
import static fr.awildelephant.rdbms.evaluator.operation.DecimalSubtraction.decimalSubtraction;
import static fr.awildelephant.rdbms.evaluator.operation.IntegerAddition.integerAddition;
import static fr.awildelephant.rdbms.evaluator.operation.IntegerDivision.integerDivision;
import static fr.awildelephant.rdbms.evaluator.operation.IntegerMultiplication.integerMultiplication;
import static fr.awildelephant.rdbms.evaluator.operation.IntegerSubtraction.integerSubtraction;
import static fr.awildelephant.rdbms.evaluator.operation.NotOperation.notOperation;
import static fr.awildelephant.rdbms.evaluator.operation.OrOperation.orOperation;
import static fr.awildelephant.rdbms.evaluator.operation.Reference.reference;
import static fr.awildelephant.rdbms.evaluator.operation.comparison.ComparisonFactory.equalComparison;
import static fr.awildelephant.rdbms.evaluator.operation.comparison.ComparisonFactory.lessComparison;
import static fr.awildelephant.rdbms.evaluator.operation.comparison.ComparisonFactory.lessOrEqualComparison;
import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;
import static fr.awildelephant.rdbms.schema.Domain.DATE;
import static fr.awildelephant.rdbms.schema.Domain.DECIMAL;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;
import static fr.awildelephant.rdbms.schema.Domain.INTERVAL;
import static fr.awildelephant.rdbms.schema.Domain.TEXT;
import static java.lang.Integer.parseInt;

public class ASTToFormulaTransformer extends DefaultASTVisitor<Operation> {

    private final Schema inputSchema;

    private final Map<String, Reference> references = new HashMap<>();

    private ASTToFormulaTransformer(Schema inputSchema) {
        this.inputSchema = inputSchema;
    }

    static Formula createFormula(AST tree, Schema inputSchema, String outputName) {
        final ASTToFormulaTransformer transformer = new ASTToFormulaTransformer(inputSchema);
        final Operation operation = transformer.apply(tree);

        return new Formula(operation, transformer.references, outputName);
    }

    @Override
    public Operation visit(And and) {
        final Operation left = apply(and.left());
        final Operation right = apply(and.right());

        return andOperation(left, right);
    }

    @Override
    public Operation visit(BooleanLiteral booleanLiteral) {
        if (booleanLiteral == BooleanLiteral.TRUE) {
            return constant(trueValue(), BOOLEAN);
        } else if (booleanLiteral == BooleanLiteral.FALSE) {
            return constant(falseValue(), BOOLEAN);
        } else {
            throw new UnsupportedOperationException("Not yet implemented"); // TODO
        }
    }

    @Override
    public Operation visit(Cast cast) {
        if (cast.targetType() != ColumnDefinition.DATE) {
            throw new UnsupportedOperationException("Unsupported cast to type " + cast.targetType());
        }

        final Operation input = apply(cast.input());

        final Domain inputDomain = input.domain();

        if (inputDomain != TEXT) {
            throw new UnsupportedOperationException("Unsupported cast from " + inputDomain + " to DATE");
        }

        return dateCastOperation(input);
    }

    @Override
    public Operation visit(ColumnName columnName) {
        return references.computeIfAbsent(columnName.name(), name -> {
            final Domain domain = inputSchema.column(name).domain();

            return reference(domain);
        });
    }

    @Override
    public Operation visit(DecimalLiteral decimalLiteral) {
        return constant(decimalValue(decimalLiteral.value()), DECIMAL);
    }

    @Override
    public Operation visit(Divide divide) {
        final Operation left = apply(divide.left());
        final Operation right = apply(divide.right());

        if (left.domain() == DECIMAL || right.domain() == DECIMAL) {
            return decimalDivision(left, right);
        }

        return integerDivision(left, right);
    }

    @Override
    public Operation visit(Equal equal) {
        final Operation left = apply(equal.left());
        final Operation right = apply(equal.right());

        return equalComparison(left, right);
    }

    @Override
    public Operation visit(Greater greater) {
        final Operation left = apply(greater.left());
        final Operation right = apply(greater.right());

        return lessComparison(right, left);
    }

    @Override
    public Operation visit(IntegerLiteral integerLiteral) {
        return constant(integerValue(integerLiteral.value()), INTEGER);
    }

    @Override
    public Operation visit(IntervalLiteral intervalLiteral) {
        final Period period = Period.ofDays(parseInt(intervalLiteral.intervalString()));

        return constant(intervalValue(period), INTERVAL);
    }

    @Override
    public Operation visit(Less less) {
        final Operation left = apply(less.left());
        final Operation right = apply(less.right());

        return lessComparison(left, right);
    }

    @Override
    public Operation visit(LessOrEqual lessOrEqual) {
        final Operation left = apply(lessOrEqual.left());
        final Operation right = apply(lessOrEqual.right());

        return lessOrEqualComparison(left, right);
    }

    @Override
    public Operation visit(Minus minus) {
        final Operation left = apply(minus.left());
        final Operation right = apply(minus.right());

        if (left.domain() == DECIMAL || right.domain() == DECIMAL) {
            return decimalSubtraction(left, right);
        }

        if (left.domain() == INTEGER && right.domain() == INTEGER) {
            return integerSubtraction(left, right);
        }

        if (left.domain() == Domain.DATE && right.domain() == INTERVAL) {
            return dateIntervalSubstraction(left, right);
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public Operation visit(Multiply multiply) {
        final Operation left = apply(multiply.left());
        final Operation right = apply(multiply.right());

        if (left.domain() == DECIMAL || right.domain() == DECIMAL) {
            return decimalMultiplication(left, right);
        }

        return integerMultiplication(left, right);
    }

    @Override
    public Operation visit(Not not) {
        return notOperation(apply(not.input()));
    }

    @Override
    public Operation visit(NullLiteral nullLiteral) {
        return constant(nullValue(), INTEGER);
    }

    @Override
    public Operation visit(Or or) {
        final Operation left = apply(or.left());
        final Operation right = apply(or.right());

        return orOperation(left, right);
    }

    @Override
    public Operation visit(Placeholder placeholder) {
        throw new UnsupportedOperationException("Placeholders are not yet supported");
    }

    @Override
    public Operation visit(Plus plus) {
        final Operation left = apply(plus.left());
        final Operation right = apply(plus.right());

        if (left.domain() == DECIMAL || right.domain() == DECIMAL) {
            return decimalAddition(left, right);
        }

        if (left.domain() == INTEGER && right.domain() == INTEGER) {
            return integerAddition(left, right);
        }

        if (left.domain() == DATE && right.domain() == INTERVAL) {
            return dateIntervalAddition(left, right);
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public Operation visit(TextLiteral textLiteral) {
        return constant(textValue(textLiteral.value()), TEXT);
    }

    @Override
    public Operation defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}
