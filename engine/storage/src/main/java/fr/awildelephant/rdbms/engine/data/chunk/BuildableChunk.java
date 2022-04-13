package fr.awildelephant.rdbms.engine.data.chunk;

import java.util.ArrayList;
import java.util.List;

public class BuildableChunk<T> implements Chunk<T> {

    private final List<T> list = new ArrayList<>();

    public void add(T element) {
        list.add(element);
    }

    @Override
    public Iterable<T> content() {
        return list;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }
}
