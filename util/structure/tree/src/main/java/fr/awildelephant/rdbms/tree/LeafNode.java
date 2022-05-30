package fr.awildelephant.rdbms.tree;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public abstract class LeafNode<T extends Tree<T>> implements Tree<T> {

    @Override
    public Collection<T> children() {
        return List.of();
    }

    @Override
    public void depthFirst(Consumer<Tree<T>> consumer) {
        consumer.accept(this);
    }
}
