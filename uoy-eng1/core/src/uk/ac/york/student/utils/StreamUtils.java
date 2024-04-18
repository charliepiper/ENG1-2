package uk.ac.york.student.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utility class for working with {@link Stream}s and {@link Iterator}s.
 * This class provides static methods to create a {@link Stream} from an {@link Iterator}.
 */
@UtilityClass
public class StreamUtils {

    /**
     * Creates a sequential {@link Stream} from the given {@link Iterator}.
     * The resulting {@link Stream} does not have a defined encounter order.
     *
     * @param iterable The {@link Iterator} to create the {@link Stream} from.
     * @param <T> The type of elements in the {@link Iterator} and resulting {@link Stream}.
     * @return A sequential {@link Stream} containing the elements of the given {@link Iterator}.
     */
    @Contract("_ -> new")
    public static <T> @NotNull Stream<T> fromIterator(@NotNull Iterator<T> iterable) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterable, 0), false);
    }

    /**
     * Creates a parallel {@link Stream} from the given {@link Iterator}.
     * The resulting {@link Stream} does not have a defined encounter order.
     *
     * @param iterable The {@link Iterator} to create the {@link Stream} from.
     * @param <T> The type of elements in the {@link Iterator} and resulting {@link Stream}.
     * @return A parallel {@link Stream} containing the elements of the given {@link Iterator}.
     */
    @Contract("_ -> new")
    public static <T> @NotNull Stream<T> parallelFromIterator(@NotNull Iterator<T> iterable) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterable, 0), true);
    }
}
