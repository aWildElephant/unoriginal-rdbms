package fr.awildelephant.rdbms.storage.data.chunk;

import java.util.List;


public class SingletonChunk<T> implements Chunk<T> {

    private final T record;

    public SingletonChunk(T record) {
        this.record = record;
    }

    @Override
    public Iterable<T> content() {
        return List.of(record);
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
