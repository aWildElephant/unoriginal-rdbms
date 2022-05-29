package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.value.Any;
import fr.awildelephant.rdbms.ast.value.Avg;
import fr.awildelephant.rdbms.ast.value.Count;
import fr.awildelephant.rdbms.ast.value.CountStar;
import fr.awildelephant.rdbms.ast.value.Divide;
import fr.awildelephant.rdbms.ast.value.Greater;
import fr.awildelephant.rdbms.ast.value.IsNull;
import fr.awildelephant.rdbms.ast.value.Max;
import fr.awildelephant.rdbms.ast.value.Min;
import fr.awildelephant.rdbms.ast.value.Minus;
import fr.awildelephant.rdbms.ast.value.Multiply;
import fr.awildelephant.rdbms.ast.value.Plus;
import fr.awildelephant.rdbms.ast.value.Sum;
import fr.awildelephant.rdbms.ast.visitor.DefaultASTVisitor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.value.Divide.divide;
import static fr.awildelephant.rdbms.ast.value.Greater.greater;
import static fr.awildelephant.rdbms.ast.value.IsNull.isNull;
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
    public AST visit(Any any) {
        return transform(any);
    }

    @Override
    public AST visit(Avg avg) {
        return transform(avg);
    }

    @Override
    public AST visit(Count count) {
        return transform(count);
    }

    @Override
    public AST visit(CountStar countStar) {
        return transform(countStar);
    }

    @Override
    public AST visit(Divide divide) {
        return divide(apply(divide.leftChild()), apply(divide.rightChild()));
    }

    @Override
    public AST visit(Greater greater) {
        return greater(apply(greater.leftChild()), apply(greater.rightChild()));
    }

    @Override
    public AST visit(IsNull isNull) {
        return isNull(apply(isNull.input()));
    }

    @Override
    public AST visit(Max max) {
        return transform(max);
    }

    @Override
    public AST visit(Min min) {
        return transform(min);
    }

    @Override
    public AST visit(Minus minus) {
        return minus(apply(minus.leftChild()), apply(minus.rightChild()));
    }

    @Override
    public AST visit(Multiply multiply) {
        return multiply(apply(multiply.leftChild()), apply(multiply.rightChild()));
    }

    @Override
    public AST visit(Plus plus) {
        return plus(apply(plus.leftChild()), apply(plus.rightChild()));
    }

    @Override
    public AST visit(Sum sum) {
        return transform(sum);
    }

    @Override
    public AST defaultVisit(AST node) {
        return node;
    }

    private AST transform(AST aggregation) {
        final String name = nameResolver.apply(aggregation);
        aggregates.put(name, aggregation);
        return unqualifiedColumnName(name);
    }
}
