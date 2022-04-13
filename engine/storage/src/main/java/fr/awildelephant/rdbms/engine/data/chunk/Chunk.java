package fr.awildelephant.rdbms.engine.data.chunk;

public interface Chunk<T> {

    Iterable<T> content();

    int size();

    boolean isEmpty();
}
