package fr.awildelephant.rdbms.tree;

import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public abstract class NAryNode<T extends Tree<T>, C extends T> implements Tree<T> {

    private final List<C> children;

    protected NAryNode(List<C> children) {
        this.children = children;
    }

    @Override
    public List<C> children() {
        return children;
    }

    @Override
    public <U> U reduce(Function<T, U> reduce, BinaryOperator<U> accumulator) {
        return children.stream().map(reduce).reduce(accumulator).orElse(null);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(children);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NAryNode<?, ?> other)) {
            return false;
        }

        return equalsNAry(other);
    }

    protected boolean equalsNAry(NAryNode<?, ?> other) {
        return Objects.equals(children, other.children);
    }
}
