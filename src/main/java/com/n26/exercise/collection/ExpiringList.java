package com.n26.exercise.collection;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface ExpiringList<V> extends Iterable<V> {

    void add(V value, long atTime);
    boolean remove(Object o);
    void clear();
    int size();
    void addExpirationListener(ExpirationListener<? extends V> listener);
    void removeExpirationListener(ExpirationListener<? extends V> listener);
    default Stream<V> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
