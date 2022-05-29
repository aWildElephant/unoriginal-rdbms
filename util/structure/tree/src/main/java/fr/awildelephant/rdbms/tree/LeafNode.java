package fr.awildelephant.rdbms.tree;

import java.util.Collection;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public abstract class LeafNode<T extends Tree<T>> implements Tree<T> {

    @Override
    public Collection<T> children() {
        return List.of();
    }

    @Override
    public <U> U reduce(Function<T, U> reduce, BinaryOperator<U> accumulator) {
        return null;
    }
}
