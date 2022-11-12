package org.untitled.phoenix.exception;

import org.jetbrains.annotations.NotNull;
import org.untitled.phoenix.component.Component;

public class ComponentException extends RuntimeException {

    protected final @NotNull Component component;

    protected final @NotNull String message;

    public ComponentException(@NotNull Component component, @NotNull String message) {
        this.component = component;
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("%s :: %s.", component.getContext(), message);
    }
}
