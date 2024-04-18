package uk.ac.york.student.utils;

import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The {@link Pair} class represents a pair of two objects of any type.
 * It is a generic class that takes two type parameters: T and U.
 * The objects are stored in the fields {@link Pair#left} and {@link Pair#right}.
 *
 * @param <T> the type of the first object in the pair
 * @param <U> the type of the second object in the pair
 */
@Getter
public class Pair<T, U> {
    /**
     * The first object in the pair.
     */
    private final T left;

    /**
     * The second object in the pair.
     */
    private final U right;

    /**
     * Constructs a new {@link Pair} with the specified objects.
     *
     * @param left the first object in the pair
     * @param right the second object in the pair
     */
    public Pair(T left, U right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Creates a new {@link Pair} with the specified objects.
     * This is a convenience static factory method.
     *
     * @param left the first object in the pair
     * @param right the second object in the pair
     * @param <T> the type of the first object in the pair
     * @param <U> the type of the second object in the pair
     * @return a new {@link Pair} with the specified objects
     */
    @Contract(value = "_, _ -> new", pure = true)
    public static <T, U> @NotNull Pair<T, U> of(T left, U right) {
        return new Pair<>(left, right);
    }

    /**
     * Returns a {@link String} representation of the {@link Pair}.
     * The string representation is of the form "({@link Pair#left}, {@link Pair#right})".
     *
     * @return a {@link String} representation of the {@link Pair}
     */
    @Override
    public String toString() {
        return "(" + left + ", " + right + ")";
    }
}
