package fr.awildelephant.rdbms.storage.data.chunk;

public final class ChunkFactory {

    private ChunkFactory() {

    }

    public static <T> Chunk<T> of() {
        return new EmptyChunk<>();
    }

    public static <T> Chunk<T> of(T element) {
        return new SingletonChunk<>(element);
    }

    public static <T> Chunk<T> of(Iterable<T> iterable, int size) {
        return new IterableChunk<>(iterable, size);
    }

    public static <T> BuildableChunk<T> buildableChunk() {
        return new BuildableChunk<>();
    }
}
