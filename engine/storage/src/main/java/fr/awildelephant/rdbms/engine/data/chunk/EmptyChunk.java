package fr.awildelephant.rdbms.engine.data.chunk;

import java.util.List;

public class EmptyChunk<T> implements Chunk<T> {

    @Override
    public Iterable<T> content() {
        return List.of();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}
