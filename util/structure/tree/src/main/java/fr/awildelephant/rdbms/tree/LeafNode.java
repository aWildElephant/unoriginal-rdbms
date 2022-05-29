package fr.awildelephant.rdbms.tree;

import java.util.Collection;
import java.util.List;

public abstract class LeafNode<T extends Tree<T>> implements Tree<T> {

    @Override
    public Collection<T> children() {
        return List.of();
    }
}
