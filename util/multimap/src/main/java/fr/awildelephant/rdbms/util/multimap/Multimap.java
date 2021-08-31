package fr.awildelephant.rdbms.util.multimap;

import java.util.*;

public final class Multimap<K, V> implements Map<K, List<V>> {

    private final Map<K, List<V>> delegate = new HashMap<>();

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object o) {
        return delegate.containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return delegate.containsValue(o);
    }

    @Override
    public List<V> get(Object o) {
        return delegate.get(o);
    }

    @Override
    public List<V> remove(Object o) {
        return delegate.remove(o);
    }

    /**
     * @return true if there is still a value associated to the given key, false otherwise
     */
    public boolean removeSingle(K key, V value) {
        final List<V> values = get(key);

        values.remove(value);

        boolean empty = values.isEmpty();

        if (empty) {
            remove(key);
        }

        return !empty;
    }

    @Override
    public List<V> put(K key, List<V> value) {
        if (value == null) {
            throw new NullPointerException();
        }

        return delegate.put(key, new ArrayList<>(value));
    }

    public void putSingle(K key, V value) {
        compute(key, (unused, values) -> {
            if (values == null) {
                values = new ArrayList<>();
            }

            values.add(value);

            return values;
        });
    }

    @Override
    public void putAll(Map<? extends K, ? extends List<V>> map) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public Set<K> keySet() {
        return delegate.keySet();
    }

    @Override
    public Collection<List<V>> values() {
        return delegate.values();
    }

    @Override
    public Set<Entry<K, List<V>>> entrySet() {
        return delegate.entrySet();
    }
}
