package fr.awildelephant.rdbms.tree;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public abstract class BinaryNode<T extends Tree<T>, L extends T, R extends T> implements Tree<T> {

    private final L leftChild;
    private final R rightChild;

    protected BinaryNode(L leftChild, R rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public L leftChild() {
        return leftChild;
    }

    public L firstChild() {
        return leftChild;
    }

    public R rightChild() {
        return rightChild;
    }

    public R secondChild() {
        return rightChild;
    }

    @Override
    public Collection<T> children() {
        return List.of(leftChild, rightChild);
    }

    @Override
    public <U> U reduce(Function<T, U> reduce, BinaryOperator<U> accumulator) {
        return accumulator.apply(reduce.apply(leftChild), reduce.apply(rightChild));
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftChild, rightChild);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final BinaryNode other)) {
            return false;
        }

        return equalsBinaryNode(other);
    }

    protected boolean equalsBinaryNode(BinaryNode<?, ?, ?> other) {
        return Objects.equals(leftChild, other.leftChild)
                && Objects.equals(rightChild, other.rightChild);
    }
}
