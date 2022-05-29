package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Cast;
import fr.awildelephant.rdbms.ast.value.BooleanLiteral;
import fr.awildelephant.rdbms.ast.value.DecimalLiteral;
import fr.awildelephant.rdbms.ast.value.IntegerLiteral;
import fr.awildelephant.rdbms.ast.value.NullLiteral;
import fr.awildelephant.rdbms.ast.value.TextLiteral;
import fr.awildelephant.rdbms.ast.visitor.DefaultASTVisitor;
import fr.awildelephant.rdbms.data.value.DomainValue;

import java.time.LocalDate;

import static fr.awildelephant.rdbms.data.value.DateValue.dateValue;
import static fr.awildelephant.rdbms.data.value.DecimalValue.decimalValue;
import static fr.awildelephant.rdbms.data.value.FalseValue.falseValue;
import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.data.value.TextValue.textValue;
import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;

public class ObjectWrapper extends DefaultASTVisitor<DomainValue> {

    @Override
    public DomainValue visit(BooleanLiteral booleanLiteral) {
        return switch (booleanLiteral.value()) {
            case TRUE -> trueValue();
            case FALSE -> falseValue();
            case UNKNOWN -> nullValue();
        };
    }

    @Override
    public DomainValue visit(Cast cast) {
        final String dateString = ((TextLiteral) cast.child()).value();

        return dateValue(LocalDate.parse(dateString));
    }

    @Override
    public DomainValue visit(DecimalLiteral decimalLiteral) {
        return decimalValue(decimalLiteral.value());
    }

    @Override
    public DomainValue visit(IntegerLiteral integerLiteral) {
        return integerValue(integerLiteral.value());
    }

    @Override
    public DomainValue visit(NullLiteral nullLiteral) {
        return nullValue();
    }

    @Override
    public DomainValue visit(TextLiteral textLiteral) {
        return textValue(textLiteral.value());
    }

    @Override
    public DomainValue defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}
