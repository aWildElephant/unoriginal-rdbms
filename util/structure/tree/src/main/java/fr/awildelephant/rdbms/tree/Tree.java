package fr.awildelephant.rdbms.tree;

import java.util.Collection;

public interface Tree<T extends Tree<T>> {

    Collection<? extends T> children();
}
