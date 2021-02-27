package fr.awildelephant.rdbms.engine.optimizer.util;

import java.util.stream.Stream;

public final class StreamHelper {

    private StreamHelper() {

    }

    public static <T> Stream<T> concat(Stream<T> a, Stream<T> b, Stream<T> c) {
        return Stream.concat(a, Stream.concat(b, c));
    }
}
