package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public record TableReferenceList(AST first, AST second, List<AST> others) implements AST {

    public static TableReferenceList tableReferenceList(AST first, AST second, List<AST> others) {
        return new TableReferenceList(first, second, others);
    }

    @Override
    public Collection<? extends AST> children() {
        final List<AST> children = new ArrayList<>();
        children.add(first);
        children.add(second);
        children.addAll(others);
        return children;
    }

    @Override
    public <U> U reduce(Function<AST, U> reduce, BinaryOperator<U> accumulator) {
        final U firstValue = reduce.apply(first);
        final U secondValue = reduce.apply(second);
        final U firstAndSecond = accumulator.apply(firstValue, secondValue);

        if (others.isEmpty()) {
            return firstAndSecond;
        } else {
            return accumulator.apply(firstAndSecond, others.stream().map(reduce).reduce(accumulator).get());
        }
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("first", first)
                .append("second", second)
                .append("others", others)
                .toString();
    }
}
