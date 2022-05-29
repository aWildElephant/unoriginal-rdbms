package fr.awildelephant.rdbms.tree;

import java.util.Collection;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public interface Tree<T extends Tree<T>> {

    Collection<? extends T> children();

    <U> U reduce(Function<T, U> reduce, BinaryOperator<U> accumulator);
}
