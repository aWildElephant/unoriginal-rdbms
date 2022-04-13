package fr.awildelephant.rdbms.engine.data.chunk;

public class IterableChunk<T> implements Chunk<T> {

    private final Iterable<T> table;
    private final int size;

    public IterableChunk(Iterable<T> table, int size) {
        this.table = table;
        this.size = size;
    }

    @Override
    public Iterable<T> content() {
        return table;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
