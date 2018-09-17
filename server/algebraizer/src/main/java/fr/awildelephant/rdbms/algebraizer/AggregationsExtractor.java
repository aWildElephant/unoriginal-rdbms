package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.value.CountStar;
import fr.awildelephant.rdbms.ast.value.DecimalLiteral;
import fr.awildelephant.rdbms.ast.value.Divide;
import fr.awildelephant.rdbms.ast.value.IntegerLiteral;
import fr.awildelephant.rdbms.ast.value.Minus;
import fr.awildelephant.rdbms.ast.value.Multiply;
import fr.awildelephant.rdbms.ast.value.NullLiteral;
import fr.awildelephant.rdbms.ast.value.Plus;
import fr.awildelephant.rdbms.ast.value.TextLiteral;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static fr.awildelephant.rdbms.ast.ColumnName.columnName;
import static fr.awildelephant.rdbms.ast.value.Divide.divide;
import static fr.awildelephant.rdbms.ast.value.Minus.minus;
import static fr.awildelephant.rdbms.ast.value.Multiply.multiply;
import static fr.awildelephant.rdbms.ast.value.Plus.plus;

public final class AggregationsExtractor extends DefaultASTVisitor<AST> {

    private final Map<String, AST> aggregates = new HashMap<>();
    private final ColumnNameResolver nameResolver;

    private AggregationsExtractor(ColumnNameResolver nameResolver) {
        this.nameResolver = nameResolver;
    }

    static AggregationsExtractor aggregationsExtractor(ColumnNameResolver resolver) {
        return new AggregationsExtractor(resolver);
    }

    Collection<AST> collectedAggregates() {
        return aggregates.values();
    }

    @Override
    public AST visit(ColumnName columnName) {
        return columnName;
    }

    @Override
    public AST visit(CountStar countStar) {
        final String name = nameResolver.apply(countStar);
        aggregates.put(name, countStar);
        return columnName(name);
    }

    @Override
    public AST visit(Divide divide) {
        return divide(apply(divide.left()), apply(divide.right()));
    }

    @Override
    public AST visit(DecimalLiteral decimalLiteral) {
        return decimalLiteral;
    }

    @Override
    public AST visit(IntegerLiteral integerLiteral) {
        return integerLiteral;
    }

    @Override
    public AST visit(NullLiteral nullLiteral) {
        return nullLiteral;
    }

    @Override
    public AST visit(Minus minus) {
        return minus(apply(minus.left()), apply(minus.right()));
    }

    @Override
    public AST visit(Multiply multiply) {
        return multiply(apply(multiply.left()), apply(multiply.right()));
    }

    @Override
    public AST visit(Plus plus) {
        return plus(apply(plus.left()), apply(plus.right()));
    }

    @Override
    public AST visit(TextLiteral textLiteral) {
        return textLiteral;
    }

    @Override
    public AST defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}
