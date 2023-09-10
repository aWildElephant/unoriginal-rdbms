package fr.awildelephant.rdbms.storage.data.chunk;

public interface Chunk<T> {

    Iterable<T> content();

    int size();

    boolean isEmpty();
}
