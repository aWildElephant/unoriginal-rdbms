package fr.awildelephant.rdbms.evaluator;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.Value;
import fr.awildelephant.rdbms.data.value.DateValue;
import fr.awildelephant.rdbms.data.value.DecimalValue;
import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.data.value.IntegerValue;
import fr.awildelephant.rdbms.data.value.StringValue;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Evaluator extends DefaultASTVisitor<DomainValue> {

    @Override
    public DomainValue visit(Value value) {
        final Object object = value.object();

        if (object instanceof Integer) {
            return new IntegerValue((int) object);
        } else if (object instanceof String) {
            return new StringValue((String) object);
        } else if (object instanceof LocalDate) {
            return new DateValue((LocalDate) object);
        } else if (object instanceof BigDecimal) {
            return new DecimalValue((BigDecimal) object);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public DomainValue defaultVisit(AST node) {
        throw new UnsupportedOperationException();
    }
}
