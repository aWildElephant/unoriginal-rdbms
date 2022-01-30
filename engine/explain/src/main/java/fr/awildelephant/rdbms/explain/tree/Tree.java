package fr.awildelephant.rdbms.explain.tree;

import java.util.List;

public record Tree<T>(T content, List<Tree<T>> children) {

    public static <T> Tree<T> leaf(T content) {
        return new Tree<>(content, List.of());
    }

    public static <T> Tree<T> node(T content, Tree<T> child) {
        return new Tree<>(content, List.of(child));
    }

    public static <T> Tree<T> node(T content, Tree<T> child1, Tree<T> child2) {
        return new Tree<>(content, List.of(child1, child2));
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }
}
