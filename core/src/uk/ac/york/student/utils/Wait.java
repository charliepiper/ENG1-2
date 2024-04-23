package uk.ac.york.student.utils;

import com.badlogic.gdx.Gdx;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for handling synchronous and asynchronous waits.
 * This class provides static methods to perform a wait operation in different ways.
 */
@UtilityClass
public final class Wait {

    /**
     * Performs a synchronous wait for the specified time and time unit.
     * If the thread is interrupted during the wait, an error is logged.
     *
     * @param time The time to wait.
     * @param timeUnit The unit of time for the wait.
     */
    public static void sync(long time, @NotNull TimeUnit timeUnit) {
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException e) {
            Gdx.app.error("Wait", "Interrupted", e);
        }
    }

    /**
     * Performs an asynchronous wait for the specified time and time unit.
     * The wait is performed in a separate thread, and a {@link CompletableFuture} is returned that completes when the wait is over.
     * If an exception occurs during the wait, an error is logged and the {@link CompletableFuture} completes exceptionally.
     *
     * @param time The time to wait.
     * @param timeUnit The unit of time for the wait.
     * @return A {@link CompletableFuture} that completes when the wait is over.
     */
    @Contract("_, _ -> new")
    public static @NotNull CompletableFuture<Void> async(long time, @NotNull TimeUnit timeUnit) {
        return CompletableFuture
            .runAsync(() -> sync(time, timeUnit))
            .exceptionally(e -> {
                Gdx.app.error("Wait", "Async Exception", e);
                return null;
            });
    }

    /**
     * Performs an asynchronous wait for the specified time and time unit, but blocks until the wait is over.
     * This method is equivalent to calling async and then immediately joining on the returned {@link CompletableFuture}.
     *
     * @param time The time to wait.
     * @param timeUnit The unit of time for the wait.
     */
    public static void asyncBlocking(long time, TimeUnit timeUnit) {
        async(time, timeUnit).join();
    }
}
