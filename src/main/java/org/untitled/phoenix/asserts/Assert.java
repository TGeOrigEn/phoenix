package org.untitled.phoenix.asserts;

import org.jetbrains.annotations.NotNull;
import org.opentest4j.AssertionFailedError;

import java.time.Duration;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class Assert {

    public static <T, V> void should(@NotNull T executor, @NotNull Function<@NotNull T, V> getter, V expected, @NotNull String message, @NotNull Duration timeout) {
        final var startTime = System.currentTimeMillis();

        while (!Objects.equals(getter.apply(executor), expected)) {
            if (System.currentTimeMillis() - startTime >= timeout.toMillis())
                throw new AssertionFailedError(message);
        }
    }


    public static <T, V> void should(@NotNull T executor, @NotNull Function<@NotNull T, V> getter, V expected, @NotNull BiPredicate<V, V> condition, @NotNull String message, @NotNull Duration timeout) {
        final var startTime = System.currentTimeMillis();

        while (!condition.test(getter.apply(executor), expected)) {
            if (System.currentTimeMillis() - startTime >= timeout.toMillis())
                throw new AssertionFailedError(message);
        }
    }
}
