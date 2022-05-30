package fr.awildelephant.rdbms.tree;

import java.util.Collection;
import java.util.function.Consumer;

public interface Tree<T extends Tree<T>> {

    Collection<? extends T> children();

    default void depthFirst(Consumer<Tree<T>> consumer) {
        children().forEach(child -> child.depthFirst(consumer));
        consumer.accept(this);
    }
}
