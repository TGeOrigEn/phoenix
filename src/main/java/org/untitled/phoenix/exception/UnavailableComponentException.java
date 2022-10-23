package org.untitled.phoenix.exception;

import org.jetbrains.annotations.NotNull;
import org.untitled.phoenix.component.Component;

public final class UnavailableComponentException extends RuntimeException {

    private final @NotNull Component component;

    public UnavailableComponentException(@NotNull Component component){
        this.component = component;
    }

    @Override
    public String toString() {
        return String.format("%s :: Был недоступен на протяжении %d миллисекунд.", component, component.getTimeout().toMillis());
    }
}
