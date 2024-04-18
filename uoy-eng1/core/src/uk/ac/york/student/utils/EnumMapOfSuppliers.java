package uk.ac.york.student.utils;

import java.util.EnumMap;
import java.util.function.Supplier;

/**
 * This class represents a map where the keys are elements of an Enum and the values are {@link Supplier}s.
 * It extends {@link EnumMap} and provides a method {@link EnumMapOfSuppliers#getResult(Enum)} to get the result produced by the Supplier associated with a key.
 *
 * @param <T> The type of {@link Enum} keys in this map.
 * @param <U> The type of values that the {@link Supplier}s in this map produce.
 */
public class EnumMapOfSuppliers<T extends Enum<T>, U> extends EnumMap<T, Supplier<U>> {

    /**
     * Constructs a new {@link EnumMapOfSuppliers} with the specified key type.
     *
     * @param keyType The class object of the key type for this map.
     */
    public EnumMapOfSuppliers(Class<T> keyType) {
        super(keyType);
    }

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
