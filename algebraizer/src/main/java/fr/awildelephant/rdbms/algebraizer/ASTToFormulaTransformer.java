package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.value.DecimalLiteral;
import fr.awildelephant.rdbms.ast.value.Divide;
import fr.awildelephant.rdbms.ast.value.IntegerLiteral;
import fr.awildelephant.rdbms.ast.value.Minus;
import fr.awildelephant.rdbms.ast.value.Multiply;
import fr.awildelephant.rdbms.ast.value.NullLiteral;
import fr.awildelephant.rdbms.ast.value.Plus;
import fr.awildelephant.rdbms.ast.value.TextLiteral;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.Reference;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.HashMap;
import java.util.Map;

import static fr.awildelephant.rdbms.data.value.DecimalValue.decimalValue;
import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;
import static fr.awildelephant.rdbms.data.value.TextValue.textValue;
import static fr.awildelephant.rdbms.evaluator.operation.Constant.constant;
import static fr.awildelephant.rdbms.evaluator.operation.DecimalAddition.decimalAddition;
import static fr.awildelephant.rdbms.evaluator.operation.DecimalDivision.decimalDivision;
import static fr.awildelephant.rdbms.evaluator.operation.DecimalMultiplication.decimalMultiplication;
import static fr.awildelephant.rdbms.evaluator.operation.DecimalSubtraction.decimalSubtraction;
import static fr.awildelephant.rdbms.evaluator.operation.IntegerAddition.integerAddition;
import static fr.awildelephant.rdbms.evaluator.operation.IntegerDivision.integerDivision;
import static fr.awildelephant.rdbms.evaluator.operation.IntegerMultiplication.integerMultiplication;
import static fr.awildelephant.rdbms.evaluator.operation.IntegerSubtraction.integerSubtraction;
import static fr.awildelephant.rdbms.evaluator.operation.Reference.reference;
import static fr.awildelephant.rdbms.schema.Domain.DECIMAL;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;
import static fr.awildelephant.rdbms.schema.Domain.TEXT;

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
    public Operation visit(IntegerLiteral integerLiteral) {
        return constant(integerValue(integerLiteral.value()), INTEGER);
    }

    @Override
    public Operation visit(TextLiteral textLiteral) {
        return constant(textValue(textLiteral.value()), TEXT);
    }

    @Override
    public Operation visit(NullLiteral nullLiteral) {
        throw new UnsupportedOperationException("Null value not supported in arithmetic expressions yet"); // TODO
    }

    @Override
    public Operation visit(Minus minus) {
        final Operation left = apply(minus.left());
        final Operation right = apply(minus.right());

        if (left.domain() == DECIMAL || right.domain() == DECIMAL) {
            return decimalSubtraction(left, right);
        }

        return integerSubtraction(left, right);
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
    public Operation visit(Plus plus) {
        final Operation left = apply(plus.left());
        final Operation right = apply(plus.right());

        if (left.domain() == DECIMAL || right.domain() == DECIMAL) {
            return decimalAddition(left, right);
        }

        return integerAddition(left, right);
    }

    @Override
    public Operation defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}
