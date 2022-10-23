package org.untitled.phoenix.exception;

import org.jetbrains.annotations.NotNull;
import org.untitled.phoenix.component.Component;

import java.time.Duration;

public final class ComponentActionException extends RuntimeException{

    private final @NotNull Component component;

    private final @NotNull Duration timeout;

    private final @NotNull String message;

    public ComponentActionException(@NotNull Component component, @NotNull String message, @NotNull Duration timeout){
        this.component = component;
        this.timeout = timeout;
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("%s :: %s на протяжении %d миллисекунд.", component, message, timeout.toMillis());
    }
}
