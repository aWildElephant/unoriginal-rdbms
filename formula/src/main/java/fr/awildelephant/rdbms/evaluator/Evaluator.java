package fr.awildelephant.rdbms.evaluator;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.IntegerLiteral;
import fr.awildelephant.rdbms.ast.NullLiteral;
import fr.awildelephant.rdbms.ast.TextLiteral;
import fr.awildelephant.rdbms.data.value.DomainValue;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.data.value.TextValue.textValue;

public class Evaluator extends DefaultASTVisitor<DomainValue> {

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
        throw new UnsupportedOperationException();
    }
}
