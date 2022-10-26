package org.untitled.phoenix.exception;

import org.untitled.phoenix.component.Component;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class ComponentActionException extends ComponentException {

    public ComponentActionException(@NotNull Component component, @NotNull String message, @NotNull Duration timeout) {
        super(component, String.format("%s на протяжении %d миллисекунд", message, timeout.toMillis()));
    }
}
