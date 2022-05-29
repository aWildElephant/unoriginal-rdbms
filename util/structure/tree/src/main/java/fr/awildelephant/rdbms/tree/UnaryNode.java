package fr.awildelephant.rdbms.tree;

import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public abstract class UnaryNode<T extends Tree<T>, C extends T> implements Tree<T> {

    private final C child;

    protected UnaryNode(C child) {
        this.child = child;
    }

    public C child() {
        return child;
    }

    @Override
    public List<C> children() {
        return List.of(child);
    }

    @Override
    public <U> U reduce(Function<T, U> reduce, BinaryOperator<U> accumulator) {
        return reduce.apply(child);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(child);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UnaryNode other)) {
            return false;
        }

        return equalsUnaryNode(other);
    }

    protected boolean equalsUnaryNode(UnaryNode<?, ?> other) {
        return Objects.equals(child, other.child);
    }
}
