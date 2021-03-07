package fr.awildelephant.rdbms.engine.optimizer.util;

import java.util.HashSet;
import java.util.Set;

public final class SetHelper {

    private SetHelper() {

    }

    public static <T> Set<T> intersection(Set<T> a, Set<T> b) {
        final Set<T> intersection = new HashSet<>(a);
        intersection.retainAll(b);
        return intersection;
    }

    public static <T> Set<T> union(Set<T> a, Set<T> b) {
        final HashSet<T> union = new HashSet<>(a);
        union.addAll(b);
        return union;
    }
}
