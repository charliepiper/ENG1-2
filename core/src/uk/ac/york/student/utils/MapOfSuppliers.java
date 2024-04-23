package uk.ac.york.student.utils;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * This class represents a map where the keys are of generic type and the values are {@link Supplier}s.
 * It extends {@link HashMap} and provides a method {@link MapOfSuppliers#getResult(Object)} to get the result produced by the {@link Supplier} associated with a key.
 *
 * @param <T> The type of keys in this map.
 * @param <U> The type of values that the {@link Supplier}s in this map produce.
 */
public class MapOfSuppliers<T, U> extends HashMap<T, Supplier<U>> {

    /**
     * Returns the result produced by the {@link Supplier} associated with the specified key.
     * If the key is not found in the map, this method returns null.
     *
     * @param key The key whose associated {@link Supplier}'s result is to be returned.
     * @return The result produced by the {@link Supplier} associated with the specified key, or null if the key is not found.
     */
    public U getResult(T key) {
        Supplier<U> uSupplier = get(key);
        return uSupplier == null ? null : uSupplier.get();
    }
}
